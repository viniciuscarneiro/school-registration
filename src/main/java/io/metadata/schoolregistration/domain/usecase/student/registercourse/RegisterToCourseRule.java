package io.metadata.schoolregistration.domain.usecase.student.registercourse;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.error.exception.CourseEnrollmentLimitException;
import io.metadata.schoolregistration.infra.error.exception.StudentAlreadyEnrolledInCourseException;
import io.metadata.schoolregistration.infra.error.exception.StudentEnrollmentLimitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterToCourseRule {
    public void executeRule(Student student, Course course) {
        student.courses().ifPresent(courses -> {
            if (courses.stream().anyMatch(c -> c.id().equals(course.id()))) {
                throw new StudentAlreadyEnrolledInCourseException("Student already enrolled in this course");
            }
            if (courses.size() >= 5) {
                throw new CourseEnrollmentLimitException("Student already enrolled in 5 courses");
            }
        });
        course.students().ifPresent(courses -> {
            if (courses.size() >= 50) {
                throw new StudentEnrollmentLimitException("Course registration limit exceeded");
            }
        });
    }
}
