package io.metadata.schoolregistration.domain.usecase.course.read;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.domain.usecase.FetchByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("fetchCourseByIdUseCase")
@RequiredArgsConstructor
public class FetchByIdUseCaseImpl implements FetchByIdUseCase<Course> {
    private final CourseGateway courseGateway;

    @Override
    public Course execute(Long courseId) {
        return courseGateway.findById(courseId);
    }
}
