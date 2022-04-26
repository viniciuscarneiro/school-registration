package io.metadata.schoolregistration.domain.usecase.course.read;

import io.metadata.schoolregistration.domain.entity.Course;

import java.util.List;

public interface FindAllWithoutStudentsUseCase {
    List<Course> execute();
}
