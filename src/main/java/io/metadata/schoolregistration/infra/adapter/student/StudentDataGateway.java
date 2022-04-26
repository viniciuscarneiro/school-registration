package io.metadata.schoolregistration.infra.adapter.student;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.gateway.StudentGateway;
import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import io.metadata.schoolregistration.infra.error.exception.EntityNotFoundException;
import io.metadata.schoolregistration.infra.repository.StudentEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudentDataGateway implements StudentGateway {
    private static final String STUDENT_NOT_FOUND_ERROR_MESSAGE = "Student with id %s not found.";
    private final StudentMapper studentMapper;
    private final StudentEntityRepository studentEntityRepository;

    @Override
    public Student persist(Student student, Boolean detailedResponse) {
        var studentEntity = studentMapper.toEntity(student);
        return studentMapper.toDomain(studentEntityRepository.save(studentEntity), detailedResponse);
    }

    @Override
    public void delete(Long studentId) {
        studentEntityRepository.deleteById(studentId);
    }

    @Override
    public Student findById(Long studentId, Boolean detailedResponse) {
        if (Boolean.TRUE.equals(detailedResponse)) {
            return mapToStudent(studentId, studentEntityRepository.findDetailedById(studentId), Boolean.TRUE);
        }
        return mapToStudent(studentId, studentEntityRepository.findById(studentId), Boolean.FALSE);
    }

    @Override
    public List<Student> findAll(Boolean detailedResponse) {
        if (Boolean.TRUE.equals(detailedResponse)) {
            return mapToStudentsList(studentEntityRepository.findAllBy(), Boolean.TRUE);
        }
        return mapToStudentsList(studentEntityRepository.findAll(), Boolean.FALSE);
    }

    @Override
    public List<Student> findAllWithoutCourses() {
        return studentEntityRepository.findByCourseEntitiesIsNull().stream()
                .map(entity -> studentMapper.toDomain(entity, Boolean.FALSE))
                .toList();
    }

    @Override
    public Boolean existsByIdentificationDocument(String identificationDocument) {
        return studentEntityRepository.existsByIdentificationDocument(identificationDocument);
    }

    @Override
    public Boolean existsById(Long studentId) {
        return studentEntityRepository.existsById(studentId);
    }

    @Override
    public List<Student> findAllWithSpecificCourse(Long courseId) {
        return studentEntityRepository.findByCourseEntitiesId(courseId).stream()
                .map(entity -> studentMapper.toDomain(entity, Boolean.TRUE))
                .toList();
    }

    private List<Student> mapToStudentsList(List<StudentEntity> studentEntities, Boolean detailed) {
        return studentEntities.stream()
                .map(courseEntity -> studentMapper.toDomain(courseEntity, detailed))
                .toList();
    }

    private Student mapToStudent(Long studentId, Optional<StudentEntity> optionalStudentEntity, Boolean detailed) {
        return optionalStudentEntity
                .map(entity -> studentMapper.toDomain(entity, detailed))
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_ERROR_MESSAGE.formatted(studentId)));
    }
}
