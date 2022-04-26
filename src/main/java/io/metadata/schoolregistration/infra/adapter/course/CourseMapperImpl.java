package io.metadata.schoolregistration.infra.adapter.course;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.CreateCourseRequest;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.UpdateCourseRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public Course toDomain(CourseEntity entity, Boolean detailed) {
        if (Boolean.TRUE.equals(detailed)) {
            var students = Optional.of(entity.getStudentEntities().stream()
                    .map(studentEntity -> new Student(
                            Optional.ofNullable(studentEntity.getId()),
                            studentEntity.getFullName(),
                            studentEntity.getEmail(),
                            studentEntity.getPhoneNumber(),
                            studentEntity.getIdentificationDocument(),
                            Optional.empty()))
                    .collect(Collectors.toSet()));
            return new Course(Optional.of(entity.getId()), entity.getName(), entity.getDescription(), students);
        } else {
            return new Course(Optional.of(entity.getId()), entity.getName(), entity.getDescription(), Optional.empty());
        }
    }

    @Override
    public Course toDomain(CreateCourseRequest request) {
        return new Course(Optional.empty(), request.getName(), request.getDescription(), Optional.empty());
    }

    @Override
    public Course toDomain(Long id, UpdateCourseRequest request) {
        return new Course(Optional.ofNullable(id), request.getName(), request.getDescription(), Optional.empty());
    }

    @Override
    public CourseEntity toEntity(Course domain) {
        var entity = new CourseEntity();
        BeanUtils.copyProperties(domain, entity);
        domain.id().ifPresent(entity::setId);
        return entity;
    }
}
