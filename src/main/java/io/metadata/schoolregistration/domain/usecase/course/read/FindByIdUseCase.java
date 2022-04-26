package io.metadata.schoolregistration.domain.usecase.course.read;

import io.metadata.schoolregistration.domain.entity.Course;

public interface FindByIdUseCase {
    Course execute(Long courseId);
}
