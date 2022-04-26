package io.metadata.schoolregistration.domain.usecase.student.create;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("createStudentUseCase")
@RequiredArgsConstructor
public class CreateUseCaseImpl implements CreateUseCase {
    private final StudentGateway studentGateway;
    private final CreateStudentRule createStudentRule;

    @Override
    public Student execute(Student student) {
        createStudentRule.executeRule(student);
        return studentGateway.persist(student, Boolean.FALSE);
    }
}
