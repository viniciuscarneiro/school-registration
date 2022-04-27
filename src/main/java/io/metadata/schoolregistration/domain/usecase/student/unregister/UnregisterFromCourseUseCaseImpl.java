package io.metadata.schoolregistration.domain.usecase.student.unregister;

import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnregisterFromCourseUseCaseImpl implements UnregisterFromCourseUseCase {

    private final StudentGateway studentGateway;
    private final CourseGateway courseGateway;

    @Override
    public void execute(Long studentId, Long courseId) {
        var student = studentGateway.findById(studentId, Boolean.TRUE);
        var course = courseGateway.findById(courseId, Boolean.FALSE);
        student.courses().ifPresent(courses -> courses.remove(course));
        studentGateway.persist(student, Boolean.FALSE);
    }
}
