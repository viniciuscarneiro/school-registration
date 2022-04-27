package io.metadata.schoolregistration.domain.usecase.student.registercourse;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterToCourseUseCaseImpl implements RegisterToCourseUseCase {

    private final StudentGateway studentGateway;
    private final CourseGateway courseGateway;
    private final RegisterToCourseRule registerToCourseRule;

    @Override
    public Student execute(Long studentId, Long courseId) {
        var student = studentGateway.findById(studentId, Boolean.TRUE);
        var course = courseGateway.findById(courseId, Boolean.FALSE);
        registerToCourseRule.executeRule(student, course);
        student.courses().ifPresent(courses -> courses.add(course));
        return studentGateway.persist(student, Boolean.TRUE);
    }
}
