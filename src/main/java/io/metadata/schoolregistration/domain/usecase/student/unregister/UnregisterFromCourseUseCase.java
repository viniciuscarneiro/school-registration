package io.metadata.schoolregistration.domain.usecase.student.unregister;

public interface UnregisterFromCourseUseCase {
    void execute(Long studentId, Long courseId);
}
