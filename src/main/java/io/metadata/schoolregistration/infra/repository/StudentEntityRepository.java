package io.metadata.schoolregistration.infra.repository;

import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEntityRepository extends CrudRepository<StudentEntity, Long> {
    List<StudentEntity> findAll();

    @EntityGraph(attributePaths = {"courseEntities"})
    List<StudentEntity> findAllBy();

    Boolean existsByIdentificationDocument(String identificationDocument);

    @EntityGraph(attributePaths = {"courseEntities"})
    List<StudentEntity> findByCourseEntitiesId(Long courseId);

    List<StudentEntity> findByCourseEntitiesIsNull();

    @EntityGraph(attributePaths = {"courseEntities"})
    Optional<StudentEntity> findDetailedById(Long studentId);
}
