package io.metadata.schoolregistration.domain.usecase.student.rule;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.usecase.student.register.RegisterToCourseRule;
import io.metadata.schoolregistration.infra.error.exception.CourseEnrollmentLimitException;
import io.metadata.schoolregistration.infra.error.exception.StudentAlreadyEnrolledInCourseException;
import io.metadata.schoolregistration.infra.error.exception.StudentEnrollmentLimitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class RegisterToCourseRuleTest {

    private final RegisterToCourseRule registerToCourseRule = new RegisterToCourseRule();

    @Test
    void should_execute_rule() {
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var registeredCourse = new Course(Optional.of(2L), courseName, courseDescription, Optional.empty());
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var registerStudent = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        var courseToRegister =
                new Course(Optional.of(courseId), courseName, courseDescription, Optional.of(Set.of(registerStudent)));
        var student = new Student(
                Optional.of(studentId),
                fullName,
                email,
                phoneNumber,
                identificationDocument,
                Optional.of(Set.of(registeredCourse)));
        Assertions.assertDoesNotThrow(() -> registerToCourseRule.executeRule(student, courseToRegister));
    }

    @Test
    void should_throws_student_already_enrolled_in_course_exception() {
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseToRegister = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var student = new Student(
                Optional.of(studentId),
                fullName,
                email,
                phoneNumber,
                identificationDocument,
                Optional.of(Set.of(courseToRegister)));
        Assertions.assertThrows(
                StudentAlreadyEnrolledInCourseException.class,
                () -> registerToCourseRule.executeRule(student, courseToRegister));
    }

    @Test
    void should_throws_course_enrollment_limit_exception() {
        var courseId = 10L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseToRegister = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        var studentCourses = new HashSet<Course>();
        for (long i = 0; i < 5; i++) {
            studentCourses.add(new Course(Optional.of(i), courseName + i, courseDescription + i, Optional.empty()));
        }
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var student = new Student(
                Optional.of(studentId),
                fullName,
                email,
                phoneNumber,
                identificationDocument,
                Optional.of(studentCourses));
        Assertions.assertThrows(
                CourseEnrollmentLimitException.class,
                () -> registerToCourseRule.executeRule(student, courseToRegister));
    }

    @Test
    void should_throws_student_enrollment_limit_exception() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseStudents = new HashSet<Student>();
        for (long i = 0; i < 50; i++) {
            courseStudents.add(new Student(
                    Optional.of(i),
                    fullName + i,
                    email + i,
                    phoneNumber + i,
                    identificationDocument + i,
                    Optional.empty()));
        }
        var courseToRegister =
                new Course(Optional.of(courseId), courseName, courseDescription, Optional.of(courseStudents));

        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());

        Assertions.assertThrows(
                StudentEnrollmentLimitException.class,
                () -> registerToCourseRule.executeRule(student, courseToRegister));
    }
}
