package io.metadata.schoolregistration.infra.adapter.student;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.CreateStudentRequest;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.UpdateStudentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentMapperImpl implements StudentMapper {
    @Override
    public Student toDomain(StudentEntity entity, Boolean detailed) {
        if (Boolean.TRUE.equals(detailed)) {
            var courses = Optional.of(entity.getCourseEntities().stream()
                    .map(courseEntity -> new Course(
                            Optional.of(courseEntity.getId()),
                            courseEntity.getName(),
                            courseEntity.getDescription(),
                            Optional.empty()))
                    .collect(Collectors.toSet()));
            return mapToStudent(entity, courses);
        } else {
            return mapToStudent(entity, Optional.empty());
        }
    }

    private Student mapToStudent(StudentEntity entity, Optional<Set<Course>> courses) {
        return new Student(
                Optional.ofNullable(entity.getId()),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getIdentificationDocument(),
                courses);
    }

    @Override
    public Student toDomain(CreateStudentRequest request) {
        return new Student(
                Optional.empty(),
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getIdentificationDocument(),
                Optional.empty());
    }

    @Override
    public Student toDomain(Long id, UpdateStudentRequest request) {
        return new Student(
                Optional.ofNullable(id),
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                null,
                Optional.empty());
    }

    @Override
    public StudentEntity toEntity(Student domain) {
        var entity = new StudentEntity();
        BeanUtils.copyProperties(domain, entity);
        domain.id().ifPresent(entity::setId);
        domain.courses().ifPresent(courses -> entity.getCourseEntities()
                .addAll(courses.stream()
                        .map(course -> {
                            var courseEntity = new CourseEntity();
                            courseEntity.setId(course.id().orElseThrow());
                            return courseEntity;
                        })
                        .collect(Collectors.toSet())));
        return entity;
    }
}
