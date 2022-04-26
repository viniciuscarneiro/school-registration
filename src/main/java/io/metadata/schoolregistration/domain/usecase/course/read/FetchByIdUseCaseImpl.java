package io.metadata.schoolregistration.domain.usecase.course.read;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("findCourseByIdUseCase")
@RequiredArgsConstructor
public class FindByIdUseCaseImpl implements FindByIdUseCase {
    private final CourseGateway courseGateway;

    @Override
    public Course execute(Long courseId) {
        return courseGateway.findById(courseId);
    }
}
