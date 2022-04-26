package io.metadata.schoolregistration.domain.usecase.student.create;

import io.metadata.schoolregistration.domain.entity.Student;

public interface CreateUseCase {
    Student execute(Student student);
}
