package io.metadata.schoolregistration.domain.usecase.student.registercourse;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterToCourseUseCaseTest {

    @Mock
    private StudentGateway studentGateway;

    @Mock
    private CourseGateway courseGateway;

    @Mock
    private RegisterToCourseRule registerToCourseRule;

    @InjectMocks
    private RegisterToCourseUseCaseImpl registerToCourseUseCase;

    @Test
    void should_register_to_course() {
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
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        when(studentGateway.findById(studentId, Boolean.TRUE)).thenReturn(student);
        when(courseGateway.findById(courseId, Boolean.FALSE)).thenReturn(courseToRegister);
        doNothing().when(registerToCourseRule).executeRule(student, courseToRegister);
        var persistedStudent = new Student(
                Optional.of(studentId),
                fullName,
                email,
                phoneNumber,
                identificationDocument,
                Optional.of(Set.of(courseToRegister)));
        when(studentGateway.persist(student, Boolean.TRUE)).thenReturn(persistedStudent);
        var result = registerToCourseUseCase.execute(studentId, courseId);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(student.id(), result.id()),
                () -> assertTrue(result.courses().isPresent()),
                () -> {
                    var coursesResult = result.courses().stream().findAny().orElseThrow();
                    assertEquals(1, coursesResult.size());
                    var courseResult = coursesResult.stream().findAny().orElseThrow();
                    assertEquals(courseToRegister, courseResult);
                    assertTrue(courseResult.students().isEmpty());
                });
        verify(studentGateway, times(1)).findById(studentId, Boolean.TRUE);
        verify(courseGateway, times(1)).findById(courseId, Boolean.FALSE);
        verify(registerToCourseRule, times(1)).executeRule(student, courseToRegister);
        verify(studentGateway, times(1)).persist(student, Boolean.TRUE);
    }
}
