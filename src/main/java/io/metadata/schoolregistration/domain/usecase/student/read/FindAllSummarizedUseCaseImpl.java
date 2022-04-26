package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.domain.usecase.FindAllUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("findAllStudentsSummarizedUseCase")
@RequiredArgsConstructor
public class FindAllSummarizedUseCaseImpl implements FindAllUseCase<Student> {

    private final StudentGateway studentGateway;

    @Override
    public List<Student> execute() {
        return studentGateway.findAll(Boolean.FALSE);
    }
}
