package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.student.read.FetchAllWithSpecificCourseUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchAllWithSpecificCoursesUseCaseTest {

    @Mock
    private StudentGateway studentGateway;

    @InjectMocks
    private FetchAllWithSpecificCourseUseCaseImpl fetchAllWithSpecificCourseUseCase;

    @Test
    void should_fetch_all_without_courses() {
        var courseId = 1L;
        when(studentGateway.findAllWithSpecificCourse(courseId)).thenReturn(List.of());
        Assertions.assertDoesNotThrow(() -> fetchAllWithSpecificCourseUseCase.execute(courseId));
        verify(studentGateway, times(1)).findAllWithSpecificCourse(courseId);
    }
}
