package io.metadata.schoolregistration.domain.usecase.student.create;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.infra.error.exception.IdentificationDocumentAlreadyExistsException;
import org.springframework.stereotype.Component;

@Component
public record CreateStudentRule(StudentGateway studentGateway) {
    public void executeRule(Student student) {
        if (Boolean.TRUE.equals(studentGateway.existsByIdentificationDocument(student.identificationDocument()))) {
            throw new IdentificationDocumentAlreadyExistsException(student.identificationDocument());
        }
    }
}
