package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.entity.Student;

public interface FindByIdUseCase {
    Student execute(Long studentId);
}
