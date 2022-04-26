package io.metadata.schoolregistration.domain.gateway;

import io.metadata.schoolregistration.domain.entity.Student;

import java.util.List;

public interface StudentGateway {

    List<Student> findAllWithoutCourses();

    Student persist(Student student, Boolean detailed);

    void delete(Long studentId);

    Student findById(Long studentId, Boolean detailed);

    List<Student> findAll(Boolean detailed);

    Boolean existsByIdentificationDocument(String identificationDocument);

    Boolean existsById(Long studentId);

    List<Student> findAllWithSpecificCourse(Long courseId);
}
