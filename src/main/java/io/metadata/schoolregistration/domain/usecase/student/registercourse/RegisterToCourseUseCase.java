package io.metadata.schoolregistration.domain.usecase.student.registercourse;

import io.metadata.schoolregistration.domain.entity.Student;

public interface RegisterToCourseUseCase {
    Student execute(Long studentId, Long courseId);
}
