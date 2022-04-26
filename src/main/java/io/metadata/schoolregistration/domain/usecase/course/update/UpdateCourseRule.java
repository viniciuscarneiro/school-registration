package io.metadata.schoolregistration.domain.usecase.course.update;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.infra.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public record UpdateCourseRule(CourseGateway courseGateway) {
    public void executeRule(Course course) {
        course.id().ifPresent(id -> {
            if (Boolean.FALSE.equals(courseGateway().existsById(id))) {
                throw new EntityNotFoundException(
                        "Course with id %s not found.".formatted(course.id().orElseThrow()));
            }
        });
    }
}
