package io.metadata.schoolregistration.infra.adapter.student;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.CreateStudentRequest;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.UpdateStudentRequest;

public interface StudentMapper {
    Student toDomain(StudentEntity entity, Boolean detailed);

    Student toDomain(CreateStudentRequest createStudentRequestRequest);

    Student toDomain(Long id, UpdateStudentRequest updateStudentRequest);

    StudentEntity toEntity(Student domain);
}
