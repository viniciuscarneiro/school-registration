package io.metadata.schoolregistration.domain.usecase.course.delete;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.infra.error.exception.CourseWithEnrolledStudentsException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public record DeleteCourseRule(CourseGateway courseGateway) {
    public void executeRule(Long courseId) {
        var course = courseGateway.findById(courseId, Boolean.TRUE);
        course.students().ifPresent(this::validateIfHasStudents);
    }

    private void validateIfHasStudents(Set<Student> students) {
        if (!students.isEmpty()) {
            throw new CourseWithEnrolledStudentsException("Course has students and can't be be deleted.");
        }
    }
}
