package io.metadata.schoolregistration.domain.usecase.course.create;

import io.metadata.schoolregistration.domain.entity.Course;

public interface CreateUseCase {
    Course execute(Course course);
}
