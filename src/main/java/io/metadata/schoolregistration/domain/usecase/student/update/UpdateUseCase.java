package io.metadata.schoolregistration.domain.usecase.student.update;

import io.metadata.schoolregistration.domain.entity.Student;

public interface UpdateUseCase {
    Student execute(Student student);
}
