package io.metadata.schoolregistration.domain.usecase.student.read;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchAllWithSpecificCourseUseCaseImpl implements FetchAllWithSpecificCourseUseCase {

    private final StudentGateway studentGateway;

    @Override
    public List<Student> execute(Long courseId) {
        return studentGateway.findAllWithSpecificCourse(courseId);
    }
}
