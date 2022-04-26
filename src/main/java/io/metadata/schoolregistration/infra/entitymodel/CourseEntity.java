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
        exclude = {"name", "description", "studentEntities"})
@ToString(exclude = {"studentEntities"})
@NoArgsConstructor
@Entity
@Table(name = "course_entity")
public class CourseEntity extends AbstractEntity {

    @Column
    private String name;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(
            name = "student_course_entity",
            joinColumns = {@JoinColumn(name = "fk_course")},
            inverseJoinColumns = {@JoinColumn(name = "fk_student")})
    private Set<StudentEntity> studentEntities = new HashSet<>();
}
