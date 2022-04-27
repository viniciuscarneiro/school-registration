package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.student.read.FetchByIdUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchByIdUseCaseTest {

    @Mock
    private StudentGateway studentGateway;

    @InjectMocks
    private FetchByIdUseCaseImpl fetchByIdUseCase;

    @Test
    void should_fetch_by_id() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        when(studentGateway.findById(studentId, Boolean.FALSE)).thenReturn(student);
        Assertions.assertDoesNotThrow(() -> fetchByIdUseCase.execute(studentId));
        verify(studentGateway, times(1)).findById(studentId, Boolean.FALSE);
    }
}
