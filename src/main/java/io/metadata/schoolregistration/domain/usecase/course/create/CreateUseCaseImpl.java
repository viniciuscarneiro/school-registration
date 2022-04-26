package io.metadata.schoolregistration.domain.usecase.course.create;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("createCourseUseCase")
@RequiredArgsConstructor
public class CreateUseCaseImpl implements CreateUseCase {
    private final CourseGateway courseGateway;

    @Override
    public Course execute(Course course) {
        return courseGateway.persist(course);
    }
}
