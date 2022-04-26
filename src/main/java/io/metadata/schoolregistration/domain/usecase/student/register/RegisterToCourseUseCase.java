package io.metadata.schoolregistration.domain.usecase.student.register;

import io.metadata.schoolregistration.domain.entity.Student;

public interface RegisterToCourseUseCase {
    Student execute(Long studentId, Long courseId);
}
