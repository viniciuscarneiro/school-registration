package io.metadata.schoolregistration.domain.usecase.student.delete;

import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("deleteStudentUseCase")
@RequiredArgsConstructor
public class DeleteUseCaseImpl implements DeleteUseCase {
    private final StudentGateway studentGateway;
    private final DeleteStudentRule deleteStudentRule;

    @Override
    public void execute(Long studentId) {
        deleteStudentRule.executeRule(studentId);
        studentGateway.delete(studentId);
    }
}
