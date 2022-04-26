package io.metadata.schoolregistration.infra.resource.v1.course.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.metadata.schoolregistration.infra.resource.v1.student.model.response.StudentModel;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "courses", itemRelation = "course")
public class CourseModel extends RepresentationModel<CourseModel> {
    private Long id;
    private String name;
    private String description;
    private Set<StudentModel> students;
}
