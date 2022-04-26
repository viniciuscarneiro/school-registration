package io.metadata.schoolregistration.domain.gateway;

import io.metadata.schoolregistration.domain.entity.Student;

import java.util.List;

public interface StudentGateway {

    Student persist(Student student, Boolean detailedResponse);

    void delete(Long studentId);

    Student findById(Long studentId, Boolean detailedResponse);

    List<Student> findAll(Boolean detailedResponse);

    List<Student> findAllWithoutCourses();

    Boolean existsByIdentificationDocument(String identificationDocument);

    Boolean existsById(Long studentId);

    List<Student> findAllWithSpecificCourse(Long courseId);
}
