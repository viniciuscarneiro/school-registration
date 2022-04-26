package io.metadata.schoolregistration.domain.gateway;

import io.metadata.schoolregistration.domain.entity.Course;

import java.util.List;

public interface CourseGateway {

    Course persist(Course course);

    void delete(Long courseId);

    Course findById(Long courseId);

    List<Course> findAll(Boolean detailed);

    Boolean existsById(Long courseId);

    List<Course> findAllForSpecificStudent(Long studentId);

    List<Course> findAllWithoutStudents();
}
