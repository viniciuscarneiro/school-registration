package io.metadata.schoolregistration.infra.repository;

import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEntityRepository extends CrudRepository<CourseEntity, Long> {

    List<CourseEntity> findAll();

    @EntityGraph(attributePaths = {"studentEntities"})
    List<CourseEntity> findAllBy();

    @EntityGraph(attributePaths = {"studentEntities"})
    List<CourseEntity> findByStudentEntitiesId(Long studentId);

    List<CourseEntity> findByStudentEntitiesIsNull();

    @EntityGraph(attributePaths = {"studentEntities"})
    Optional<CourseEntity> findDetailedById(Long courseId);
}
