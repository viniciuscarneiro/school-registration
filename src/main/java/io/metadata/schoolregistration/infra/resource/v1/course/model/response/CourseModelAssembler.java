package io.metadata.schoolregistration.infra.resource.v1.course.model.response;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.resource.v1.course.CourseResource;
import io.metadata.schoolregistration.infra.resource.v1.student.StudentResource;
import io.metadata.schoolregistration.infra.resource.v1.student.model.response.StudentModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CourseModelAssembler extends RepresentationModelAssemblerSupport<Course, CourseModel> {

    public CourseModelAssembler() {
        super(CourseResource.class, CourseModel.class);
    }

    @Override
    public CourseModel toModel(Course entity) {
        var model = instantiateModel(entity);
        model.add(
                linkTo(methodOn(CourseResource.class).findById(entity.id().orElseThrow()))
                        .withSelfRel(),
                linkTo(methodOn(CourseResource.class).findAll(false)).withRel("courses"));
        entity.id().ifPresent(model::setId);
        model.setName(entity.name());
        model.setDescription(entity.description());
        entity.students().ifPresent(courses -> model.setStudents(toStudentModel(courses)));
        return model;
    }

    @Override
    public CollectionModel<CourseModel> toCollectionModel(Iterable<? extends Course> entities) {
        return toCollectionModel(entities, null);
    }

    public CollectionModel<CourseModel> toCollectionModel(Iterable<? extends Course> entities, Link selfLink) {
        var entityModels = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .toList();
        return CollectionModel.of(
                entityModels,
                List.of(Optional.ofNullable(selfLink)
                        .orElse(linkTo(methodOn(CourseResource.class).findAll(false))
                                .withSelfRel())));
    }

    private Set<StudentModel> toStudentModel(Set<Student> courses) {
        if (courses.isEmpty()) {
            return Set.of();
        }

        return courses.stream()
                .map(student -> StudentModel.builder()
                        .id(student.id().orElseThrow())
                        .fullName(student.fullName())
                        .email(student.email())
                        .phoneNumber(student.phoneNumber())
                        .identificationDocument(student.identificationDocument())
                        .build()
                        .add(linkTo(methodOn(StudentResource.class)
                                        .findById(student.id().orElseThrow()))
                                .withSelfRel()))
                .collect(Collectors.toSet());
    }
}
