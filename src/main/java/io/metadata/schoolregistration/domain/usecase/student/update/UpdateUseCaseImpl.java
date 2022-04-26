package io.metadata.schoolregistration.domain.usecase.student.update;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("updateStudentUseCase")
@RequiredArgsConstructor
public class UpdateUseCaseImpl implements UpdateUseCase {
    private final StudentGateway studentGateway;
    private final UpdateStudentRule updateStudentRule;

    @Override
    public Student execute(Student student) {
        var studentToBeUpdated = updateStudentRule.executeRule(student);
        return studentGateway.persist(studentToBeUpdated, Boolean.FALSE);
    }
}
