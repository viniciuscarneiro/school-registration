package io.metadata.schoolregistration.infra.adapter.course;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.error.exception.EntityNotFoundException;
import io.metadata.schoolregistration.infra.repository.CourseEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourseDataGateway implements CourseGateway {
    private static final String COURSE_NOT_FOUND_ERROR_MESSAGE = "Course with id %s not found.";
    private final CourseMapper courseMapper;
    private final CourseEntityRepository courseEntityRepository;

    @Override
    public Course persist(Course course) {
        var courseEntity = courseMapper.toEntity(course);
        return courseMapper.toDomain(courseEntityRepository.save(courseEntity), Boolean.FALSE);
    }

    @Override
    public void delete(Long courseId) {
        courseEntityRepository.deleteById(courseId);
    }

    @Override
    public Boolean existsById(Long courseId) {
        return courseEntityRepository.existsById(courseId);
    }

    @Override
    public Course findById(Long courseId, Boolean detailedResponse) {
        if (Boolean.TRUE.equals(detailedResponse)) {
            return mapToCourse(courseId, courseEntityRepository.findDetailedById(courseId), Boolean.TRUE);
        }
        return mapToCourse(courseId, courseEntityRepository.findById(courseId), Boolean.FALSE);
    }

    @Override
    public List<Course> findAll(Boolean detailedResponse) {
        if (Boolean.TRUE.equals(detailedResponse)) {
            return mapToCoursesList(courseEntityRepository.findAllBy(), Boolean.TRUE);
        }
        return mapToCoursesList(courseEntityRepository.findAll(), Boolean.FALSE);
    }

    @Override
    public List<Course> findAllForSpecificStudent(Long studentId) {
        return courseEntityRepository.findByStudentEntitiesId(studentId).stream()
                .map(courseEntity -> courseMapper.toDomain(courseEntity, Boolean.TRUE))
                .toList();
    }

    @Override
    public List<Course> findAllWithoutStudents() {
        return courseEntityRepository.findByStudentEntitiesIsNull().stream()
                .map(courseEntity -> courseMapper.toDomain(courseEntity, Boolean.FALSE))
                .toList();
    }

    private List<Course> mapToCoursesList(List<CourseEntity> courseEntities, Boolean detailed) {
        return courseEntities.stream()
                .map(courseEntity -> courseMapper.toDomain(courseEntity, detailed))
                .toList();
    }

    private Course mapToCourse(Long courseId, Optional<CourseEntity> optionalCourseEntity, Boolean detailed) {
        return optionalCourseEntity
                .map(courseEntity -> courseMapper.toDomain(courseEntity, detailed))
                .orElseThrow(() -> new EntityNotFoundException(COURSE_NOT_FOUND_ERROR_MESSAGE.formatted(courseId)));
    }
}
