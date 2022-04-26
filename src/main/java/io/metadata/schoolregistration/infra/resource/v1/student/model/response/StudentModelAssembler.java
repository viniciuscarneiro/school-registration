package io.metadata.schoolregistration.infra.resource.v1.student.model.response;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.resource.v1.course.CourseResource;
import io.metadata.schoolregistration.infra.resource.v1.course.model.response.CourseModel;
import io.metadata.schoolregistration.infra.resource.v1.student.StudentResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentModelAssembler extends RepresentationModelAssemblerSupport<Student, StudentModel> {

    public StudentModelAssembler() {
        super(StudentResource.class, StudentModel.class);
    }

    @Override
    public StudentModel toModel(Student entity) {
        var model = instantiateModel(entity);
        model.add(
                linkTo(methodOn(StudentResource.class).findById(entity.id().orElseThrow()))
                        .withSelfRel(),
                linkTo(methodOn(StudentResource.class).findAll(false)).withRel("students"));
        entity.id().ifPresent(model::setId);
        model.setFullName(entity.fullName());
        model.setEmail(entity.email());
        model.setPhoneNumber(entity.phoneNumber());
        model.setIdentificationDocument(entity.identificationDocument());
        entity.courses().ifPresent(courses -> model.setCourses(toCoursesModel(courses)));
        return model;
    }

    @Override
    public CollectionModel<StudentModel> toCollectionModel(Iterable<? extends Student> entities) {
        var entityModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(
                entityModels,
                List.of(linkTo(methodOn(StudentResource.class).findAll(false)).withSelfRel()));
    }

    private Set<CourseModel> toCoursesModel(Set<Course> courses) {
        if (courses.isEmpty()) {
            return Set.of();
        }
        return courses.stream()
                .map(course -> CourseModel.builder()
                        .id(course.id().orElseThrow())
                        .name(course.name())
                        .description(course.description())
                        .build()
                        .add(linkTo(methodOn(CourseResource.class)
                                        .findById(course.id().orElseThrow()))
                                .withSelfRel()))
                .collect(Collectors.toSet());
    }
}
