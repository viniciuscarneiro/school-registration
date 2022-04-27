package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.student.read.FetchAllWithoutCoursesUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchAllWithoutCoursesUseCaseTest {

    @Mock
    private StudentGateway studentGateway;

    @InjectMocks
    private FetchAllWithoutCoursesUseCaseImpl fetchAllWithoutCoursesUseCase;

    @Test
    void should_fetch_all_without_courses() {
        when(studentGateway.findAllWithoutCourses()).thenReturn(List.of());
        Assertions.assertDoesNotThrow(() -> fetchAllWithoutCoursesUseCase.execute());
        verify(studentGateway, times(1)).findAllWithoutCourses();
    }
}
