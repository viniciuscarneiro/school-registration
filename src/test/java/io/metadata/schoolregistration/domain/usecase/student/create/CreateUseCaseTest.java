package io.metadata.schoolregistration.domain.usecase.student.create;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.infra.error.exception.IdentificationDocumentAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUseCaseTest {

    @Test
    void should_create_student() {
        StudentGateway studentGateway = Mockito.mock(StudentGateway.class);
        CreateStudentRule createStudentRule = new CreateStudentRule(studentGateway);
        CreateUseCaseImpl createUseCase = new CreateUseCaseImpl(studentGateway, createStudentRule);
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var student =
                new Student(Optional.empty(), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        Mockito.when(studentGateway.existsByIdentificationDocument(identificationDocument))
                .thenReturn(Boolean.FALSE);
        var persistedStudent =
                new Student(Optional.of(1L), fullName, email, phoneNumber, identificationDocument, Optional.empty());

        when(studentGateway.persist(student, Boolean.FALSE)).thenReturn(persistedStudent);
        var result = createUseCase.execute(student);
        Assertions.assertEquals(fullName, result.fullName());
        Assertions.assertEquals(email, result.email());
        Assertions.assertEquals(identificationDocument, result.identificationDocument());
        Assertions.assertEquals(phoneNumber, result.phoneNumber());
        verify(studentGateway, times(1)).existsByIdentificationDocument(identificationDocument);
        verify(studentGateway, times(1)).persist(student, Boolean.FALSE);
    }

    @Test
    void should_not_create_student_when_identification_document_already_exists() {
        StudentGateway studentGateway = Mockito.mock(StudentGateway.class);
        CreateStudentRule createStudentRule = new CreateStudentRule(studentGateway);
        CreateUseCaseImpl createUseCase = new CreateUseCaseImpl(studentGateway, createStudentRule);
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        Mockito.when(studentGateway.existsByIdentificationDocument(identificationDocument))
                .thenReturn(Boolean.TRUE);
        Assertions.assertThrows(
                IdentificationDocumentAlreadyExistsException.class, () -> createUseCase.execute(student));
        verify(studentGateway, times(1)).existsByIdentificationDocument(identificationDocument);
        verify(studentGateway, times(0)).persist(student, Boolean.FALSE);
    }
}
