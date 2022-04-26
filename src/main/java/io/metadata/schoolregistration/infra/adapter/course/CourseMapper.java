package io.metadata.schoolregistration.infra.adapter.course;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.CreateCourseRequest;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.UpdateCourseRequest;

public interface CourseMapper {
    Course toDomain(CourseEntity entity, Boolean detailed);

    Course toDomain(CreateCourseRequest request);

    Course toDomain(Long id, UpdateCourseRequest request);

    CourseEntity toEntity(Course domain);
}
