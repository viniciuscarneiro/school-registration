package io.metadata.schoolregistration.domain.usecase.student;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.student.unregister.UnregisterFromCourseUseCaseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnregisterToCourseUseCaseTest {

    @Mock
    private StudentGateway studentGateway;

    @Mock
    private CourseGateway courseGateway;

    @InjectMocks
    private UnregisterFromCourseUseCaseImpl unregisterFromCourseUseCase;

    @Test
    void should_unregister_from_course() {
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseToUnregister = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentCourses = new HashSet<Course>();
        studentCourses.add(courseToUnregister);
        var studentWithCourse = new Student(
                Optional.of(studentId),
                fullName,
                email,
                phoneNumber,
                identificationDocument,
                Optional.of(studentCourses));

        var studentWithoutCourse = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.of(Set.of()));
        when(studentGateway.findById(studentId, Boolean.TRUE)).thenReturn(studentWithCourse);
        when(courseGateway.findById(courseId)).thenReturn(courseToUnregister);
        assertDoesNotThrow(() -> unregisterFromCourseUseCase.execute(studentId, courseId));
        verify(studentGateway, times(1)).findById(studentId, Boolean.TRUE);
        verify(courseGateway, times(1)).findById(courseId);
        verify(studentGateway, times(1)).persist(studentWithoutCourse, Boolean.FALSE);
    }
}
