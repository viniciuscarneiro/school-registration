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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CourseResource {
    private final CreateUseCase createUseCase;
    private final UpdateUseCase updateUseCase;
    private final DeleteUseCase deleteUseCase;
    @Qualifier("fetchCourseByIdUseCase")
    private final FetchByIdUseCase<Course> fetchCourseByIdUseCase;
    @Qualifier("findAllSummarizedCoursesUseCase")
    private final FetchAllUseCase<Course> findAllSummarizedCoursesUseCase;
    @Qualifier("findAllDetailedCoursesUseCase")
    private final FetchAllUseCase<Course> findAllDetailedCoursesUseCase;
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
        final URI uri = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")
                .buildAndExpand(createdCourse.id())
                .toUri();
        return ResponseEntity.created(uri).body(courseModelAssembler.toModel(createdCourse));
    }

    @PutMapping(
            value = "/courses/{id}",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<CourseModel> update(
            @PathVariable Long id, @Valid @RequestBody UpdateCourseRequest updateCourseRequest) {
        var course = updateUseCase.execute(courseMapper.toDomain(id, updateCourseRequest));
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).body(courseModelAssembler.toModel(course));
    }

    @DeleteMapping(value = "/courses/{id}")
    public ResponseEntity<CourseModel> delete(@PathVariable Long id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseModel> findById(@PathVariable Long id) {
        var result = fetchCourseByIdUseCase.execute(id);
        return ResponseEntity.ok(courseModelAssembler.toModel(result));
    }

    @GetMapping("/courses")
    public ResponseEntity<CollectionModel<CourseModel>> findAll(
            @RequestParam(value = "detailed", defaultValue = "false") Boolean detailed) {
        var result = new ArrayList<Course>();
        if (Boolean.TRUE.equals(detailed)) {
            result.addAll(findAllDetailedCoursesUseCase.execute());
        } else {
            result.addAll(findAllSummarizedCoursesUseCase.execute());
        }
        return ResponseEntity.ok(courseModelAssembler.toCollectionModel(result));
    }

    @GetMapping("/courses/students/{student_id}")
    public ResponseEntity<CollectionModel<CourseModel>> findAllForSpecificStudent(
            @PathVariable("student_id") Long studentId) {
        var courses = findAllForSpecificStudentUseCase.execute(studentId);
        return ResponseEntity.ok(courseModelAssembler.toCollectionModel(courses));
    }

    @GetMapping("/courses/without-students")
    public ResponseEntity<CollectionModel<CourseModel>> findAllWithoutStudents() {
        var courses = findAllWithoutStudentsUseCase.execute();
        return ResponseEntity.ok(courseModelAssembler.toCollectionModel(courses));
    }
}
