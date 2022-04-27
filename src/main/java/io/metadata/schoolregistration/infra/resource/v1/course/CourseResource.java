package io.metadata.schoolregistration.infra.resource.v1.course;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.usecase.FetchAllUseCase;
import io.metadata.schoolregistration.domain.usecase.FetchByIdUseCase;
import io.metadata.schoolregistration.domain.usecase.course.create.CreateUseCase;
import io.metadata.schoolregistration.domain.usecase.course.delete.DeleteUseCase;
import io.metadata.schoolregistration.domain.usecase.course.read.FindAllForSpecificStudentUseCase;
import io.metadata.schoolregistration.domain.usecase.course.read.FindAllWithoutStudentsUseCase;
import io.metadata.schoolregistration.domain.usecase.course.update.UpdateUseCase;
import io.metadata.schoolregistration.infra.adapter.course.CourseMapper;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.CreateCourseRequest;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.UpdateCourseRequest;
import io.metadata.schoolregistration.infra.resource.v1.course.model.response.CourseModel;
import io.metadata.schoolregistration.infra.resource.v1.course.model.response.CourseModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CourseResource {
    private final CreateUseCase createUseCase;
    private final UpdateUseCase updateUseCase;
    private final DeleteUseCase deleteUseCase;

    @Qualifier("fetchCourseByIdUseCase")
    private final FetchByIdUseCase<Course> fetchByIdUseCase;

    @Qualifier("fetchAllSummarizedCoursesUseCase")
    private final FetchAllUseCase<Course> fetchAllSummarizeUseCase;

    @Qualifier("fetchAllDetailedCoursesUseCase")
    private final FetchAllUseCase<Course> fetchAllDetailedUseCase;

    private final FindAllForSpecificStudentUseCase findAllForSpecificStudentUseCase;
    private final FindAllWithoutStudentsUseCase findAllWithoutStudentsUseCase;
    private final CourseModelAssembler courseModelAssembler;
    private final CourseMapper courseMapper;

    @PostMapping(
            value = "/courses",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<CourseModel> create(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        var createdCourse = createUseCase.execute(courseMapper.toDomain(createCourseRequest));
        var entityModel = courseModelAssembler.toModel(createdCourse);
        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping(
            value = "/courses/{id}",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<CourseModel> update(
            @PathVariable Long id, @Valid @RequestBody UpdateCourseRequest updateCourseRequest) {
        var course = updateUseCase.execute(courseMapper.toDomain(id, updateCourseRequest));
        var entityModel = courseModelAssembler.toModel(course);
        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping(value = "/courses/{id}")
    public ResponseEntity<CourseModel> delete(@PathVariable Long id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseModel> findById(@PathVariable Long id) {
        var result = fetchByIdUseCase.execute(id);
        return ResponseEntity.ok(courseModelAssembler.toModel(result));
    }

    @GetMapping("/courses")
    public ResponseEntity<CollectionModel<CourseModel>> findAll(
            @RequestParam(value = "detailed", defaultValue = "false") Boolean detailed) {
        var result = new ArrayList<Course>();
        if (Boolean.TRUE.equals(detailed)) {
            result.addAll(fetchAllDetailedUseCase.execute());
        } else {
            result.addAll(fetchAllSummarizeUseCase.execute());
        }
        return ResponseEntity.ok(courseModelAssembler.toCollectionModel(result));
    }

    @GetMapping("/courses/students/{student_id}")
    public ResponseEntity<CollectionModel<CourseModel>> findAllForSpecificStudent(
            @PathVariable("student_id") Long studentId) {
        var courses = findAllForSpecificStudentUseCase.execute(studentId);
        var entityModel = courseModelAssembler.toCollectionModel(
                courses,
                linkTo(methodOn(CourseResource.class).findAllForSpecificStudent(studentId)).withSelfRel());
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/courses/without-students")
    public ResponseEntity<CollectionModel<CourseModel>> findAllWithoutStudents() {
        var courses = findAllWithoutStudentsUseCase.execute();
        var entityModel = courseModelAssembler.toCollectionModel(
                courses,
                linkTo(methodOn(CourseResource.class).findAllWithoutStudents()).withSelfRel());
        return ResponseEntity.ok(entityModel);
    }
}
