package io.metadata.schoolregistration.domain.usecase.student;

import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.student.read.FetchAllDetailedUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchAllDetailedUseCaseTest {

    @Mock
    private StudentGateway studentGateway;

    @InjectMocks
    private FetchAllDetailedUseCaseImpl findAllDetailedUseCase;

    @Test
    void should_find_all() {
        when(studentGateway.findAll(Boolean.TRUE)).thenReturn(List.of());
        Assertions.assertDoesNotThrow(() -> findAllDetailedUseCase.execute());
        verify(studentGateway, times(1)).findAll(Boolean.TRUE);
    }
}
