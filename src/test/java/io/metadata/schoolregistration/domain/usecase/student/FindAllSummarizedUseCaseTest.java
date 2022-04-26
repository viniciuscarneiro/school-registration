package io.metadata.schoolregistration.domain.usecase.student;

import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.student.read.FindAllSummarizedUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllSummarizedUseCaseTest {

    @Mock
    private StudentGateway studentGateway;

    @InjectMocks
    private FindAllSummarizedUseCaseImpl findAllSummarizedUseCase;

    @Test
    void should_find_all() {
        when(studentGateway.findAll(Boolean.FALSE)).thenReturn(List.of());
        Assertions.assertDoesNotThrow(() -> findAllSummarizedUseCase.execute());
        verify(studentGateway, times(1)).findAll(Boolean.FALSE);
    }
}
