package io.metadata.schoolregistration.domain.usecase.course.update;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("updateCourseUseCase")
@RequiredArgsConstructor
public class UpdateUseCaseImpl implements UpdateUseCase {
    private final CourseGateway courseGateway;
    private final UpdateCourseRule updateCourseRule;

    @Override
    public Course execute(Course course) {
        updateCourseRule.executeRule(course);
        return courseGateway.persist(course);
    }
}
