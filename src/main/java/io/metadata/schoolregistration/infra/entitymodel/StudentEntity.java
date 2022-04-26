package io.metadata.schoolregistration.infra.entitymodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(
        callSuper = true,
        exclude = {"fullName", "email", "phoneNumber", "courseEntities"})
@ToString(exclude = {"courseEntities"})
@NoArgsConstructor
@Entity
@Table(
        name = "student_entity",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "identification_document_unique_constraint",
                        columnNames = {"identification_document"}))
public class StudentEntity extends AbstractEntity {

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "identification_document", updatable = false)
    private String identificationDocument;

    @ManyToMany()
    @JoinTable(
            name = "student_course_entity",
            joinColumns = {@JoinColumn(name = "fk_student")},
            inverseJoinColumns = {@JoinColumn(name = "fk_course")})
    private Set<CourseEntity> courseEntities = new HashSet<>();
}
