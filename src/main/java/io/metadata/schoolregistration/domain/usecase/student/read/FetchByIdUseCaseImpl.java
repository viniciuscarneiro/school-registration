package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("findStudentByIdUseCase")
@RequiredArgsConstructor
public class FindByIdUseCaseImpl implements FindByIdUseCase {
    private final StudentGateway studentGateway;

    @Override
    public Student execute(Long studentId) {
        return studentGateway.findById(studentId, Boolean.FALSE);
    }
}
