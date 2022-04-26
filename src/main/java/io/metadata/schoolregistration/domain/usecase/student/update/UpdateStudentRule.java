package io.metadata.schoolregistration.domain.usecase.student.update;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.infra.error.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public record UpdateStudentRule(StudentGateway studentGateway) {

    private static final String REQUIRED_ID_TO_UPDATE_STUDENT_ERROR_MESSAGE =
            "Id must be provided to update any student";

    public Student executeRule(Student student) {

        return student.id()
                .map(studentId -> {
                    var foundStudent = studentGateway.findById(studentId, Boolean.FALSE);
                    return new Student(
                            student.id(),
                            student.fullName(),
                            student.email(),
                            student.phoneNumber(),
                            foundStudent.identificationDocument(),
                            foundStudent.courses());
                })
                .orElseThrow(() -> new BusinessException(REQUIRED_ID_TO_UPDATE_STUDENT_ERROR_MESSAGE));
    }
}
