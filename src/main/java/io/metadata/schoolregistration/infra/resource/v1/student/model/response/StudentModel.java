package io.metadata.schoolregistration.infra.resource.v1.student.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.metadata.schoolregistration.infra.resource.v1.course.model.response.CourseModel;
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
@Relation(collectionRelation = "students", itemRelation = "student")
public class StudentModel extends RepresentationModel<StudentModel> {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("identification_document")
    private String identificationDocument;

    @JsonProperty("courses")
    private Set<CourseModel> courses;
}
