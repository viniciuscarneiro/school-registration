package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.entity.Student;

import java.util.List;

public interface FetchAllWithoutCoursesUseCase {
    List<Student> execute();
}
