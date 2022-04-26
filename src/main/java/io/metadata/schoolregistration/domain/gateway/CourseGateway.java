package io.metadata.schoolregistration.domain.gateway;

import io.metadata.schoolregistration.domain.entity.Course;

import java.util.List;

public interface CourseGateway {

    Course persist(Course course);

    void delete(Long courseId);

    Boolean existsById(Long courseId);

    Course findById(Long courseId);

    List<Course> findAll(Boolean detailed);

    List<Course> findAllForSpecificStudent(Long studentId);

    List<Course> findAllWithoutStudents();
}
