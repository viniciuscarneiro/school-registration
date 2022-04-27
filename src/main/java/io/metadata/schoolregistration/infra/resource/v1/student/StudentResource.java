package io.metadata.schoolregistration.infra.resource.v1.student;

import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.domain.usecase.FetchAllUseCase;
import io.metadata.schoolregistration.domain.usecase.student.create.CreateUseCase;
import io.metadata.schoolregistration.domain.usecase.student.delete.DeleteUseCase;
import io.metadata.schoolregistration.domain.usecase.student.read.FetchAllWithSpecificCourseUseCase;
import io.metadata.schoolregistration.domain.usecase.student.read.FetchAllWithoutCoursesUseCase;
import io.metadata.schoolregistration.domain.usecase.FetchByIdUseCase;
import io.metadata.schoolregistration.domain.usecase.student.registercourse.RegisterToCourseUseCase;
import io.metadata.schoolregistration.domain.usecase.student.unregister.UnregisterFromCourseUseCase;
import io.metadata.schoolregistration.domain.usecase.student.update.UpdateUseCase;
import io.metadata.schoolregistration.infra.adapter.student.StudentMapper;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.CreateStudentRequest;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.UpdateStudentRequest;
import io.metadata.schoolregistration.infra.resource.v1.student.model.response.StudentModel;
import io.metadata.schoolregistration.infra.resource.v1.student.model.response.StudentModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class StudentResource {
    private final CreateUseCase createUseCase;
    private final UpdateUseCase updateUseCase;
    private final DeleteUseCase deleteUseCase;
    private final FetchAllWithoutCoursesUseCase fetchAllWithoutCoursesUseCase;
    @Qualifier("fetchStudentByIdUseCase")
    private final FetchByIdUseCase<Student> fetchByIdUseCase;
    @Qualifier("fetchAllSummarizedStudentsUseCase")
    private final FetchAllUseCase<Student> fetchAllSummarizedUseCase;
    @Qualifier("fetchAllDetailedStudentsUseCase")
    private final FetchAllUseCase<Student> fetchAllDetailedUseCase;
    private final RegisterToCourseUseCase registerToCourseUseCase;
    private final FetchAllWithSpecificCourseUseCase fetchAllWithSpecificCourseUseCase;
    private final UnregisterFromCourseUseCase unregisterFromCourseUseCase;
    private final StudentModelAssembler studentModelAssembler;
    private final StudentMapper studentMapper;

    @PostMapping(
            value = "/students",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<StudentModel> create(@Valid @RequestBody CreateStudentRequest createStudentRequestRequest) {
        var createdStudent = createUseCase.execute(studentMapper.toDomain(createStudentRequestRequest));
        var entityModel = studentModelAssembler.toModel(createdStudent);
        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping(
            value = "/students/{id}",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<StudentModel> updateStudent(
            @PathVariable Long id, @Valid @RequestBody UpdateStudentRequest updateStudentRequest) {
        var updatedStudent = updateUseCase.execute(studentMapper.toDomain(id, updateStudentRequest));
        var entityModel = studentModelAssembler.toModel(updatedStudent);
        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping(value = "/students/{id}")
    public ResponseEntity<StudentModel> deleteStudent(@PathVariable Long id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentModel> findById(@PathVariable Long id) {
        var foundStudent = fetchByIdUseCase.execute(id);
        return ResponseEntity.ok(studentModelAssembler.toModel(foundStudent));
    }

    @GetMapping("/students")
    public ResponseEntity<CollectionModel<StudentModel>> findAll(
            @RequestParam(value = "detailed", defaultValue = "false") Boolean detailed) {
        var result = new ArrayList<Student>();
        if (Boolean.TRUE.equals(detailed)) {
            result.addAll(fetchAllDetailedUseCase.execute());
        } else {
            result.addAll(fetchAllSummarizedUseCase.execute());
        }
        return ResponseEntity.ok(studentModelAssembler.toCollectionModel(result));
    }

    @GetMapping("/students/without-courses")
    public ResponseEntity<CollectionModel<StudentModel>> findStudentsWithoutCourses() {
        var foundStudents = fetchAllWithoutCoursesUseCase.execute();
        return ResponseEntity.ok(studentModelAssembler.toCollectionModel(foundStudents));
    }

    @PutMapping(
            value = "/students/{id}/courses/{course_id}",
            produces = {"application/json"})
    public ResponseEntity<StudentModel> registerToCourse(
            @PathVariable Long id, @PathVariable("course_id") Long courseId) {

        var updatedStudent = registerToCourseUseCase.execute(id, courseId);
        var entityModel = studentModelAssembler.toModel(updatedStudent);
        return ResponseEntity.created(
                        entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/students/courses/{course_id}")
    public ResponseEntity<CollectionModel<StudentModel>> findStudentsWithSpecificCourse(
            @PathVariable("course_id") Long courseId) {
        var foundStudents = fetchAllWithSpecificCourseUseCase.execute(courseId);
        return ResponseEntity.ok(studentModelAssembler.toCollectionModel(foundStudents));
    }

    @DeleteMapping(value = "/students/{id}/courses/{course_id}")
    public ResponseEntity<StudentModel> unregisterFromCourse(
            @PathVariable Long id, @PathVariable("course_id") Long courseId) {
        unregisterFromCourseUseCase.execute(id, courseId);
        return ResponseEntity.noContent().build();
    }
}
