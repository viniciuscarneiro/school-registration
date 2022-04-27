package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.FetchByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("fetchStudentByIdUseCase")
@RequiredArgsConstructor
public class FetchByIdUseCaseImpl implements FetchByIdUseCase<Student> {
    private final StudentGateway studentGateway;

    @Override
    public Student execute(Long studentId) {
        return studentGateway.findById(studentId, Boolean.FALSE);
    }
}
