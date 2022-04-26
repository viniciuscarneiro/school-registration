package io.metadata.schoolregistration.domain.usecase.student.delete;

import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.infra.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public record DeleteStudentRule(StudentGateway studentGateway) {
    public void executeRule(Long studentId) {
        if (Boolean.FALSE.equals(studentGateway.existsById(studentId))) {
            throw new EntityNotFoundException("Student with id %s not found.".formatted(studentId));
        }
    }
}
