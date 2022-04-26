package io.metadata.schoolregistration.domain.usecase.course.update;

import io.metadata.schoolregistration.domain.entity.Course;

public interface UpdateUseCase {
    Course execute(Course course);
}
