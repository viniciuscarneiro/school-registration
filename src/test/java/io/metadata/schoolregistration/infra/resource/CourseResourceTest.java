package io.metadata.schoolregistration.infra.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import io.metadata.schoolregistration.infra.repository.CourseEntityRepository;
import io.metadata.schoolregistration.infra.repository.StudentEntityRepository;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.CreateCourseRequest;
import io.metadata.schoolregistration.infra.resource.v1.course.model.request.UpdateCourseRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CourseResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseEntityRepository courseEntityRepository;

    @Autowired
    private StudentEntityRepository studentEntityRepository;

    @Test
    @DirtiesContext
    void should_create_course() throws Exception {
        var expectedResult =
                "{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"},\"courses\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}";
        var name = "TDD Course";
        var description = "It will start soon, register right now!";
        var request = new CreateCourseRequest();
        request.setName(name);
        request.setDescription(description);
        mockMvc.perform(post("/v1/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_update_course() throws Exception {
        var expectedResult =
                "{\"id\":1,\"name\":\"TDD out of the box!\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"},\"courses\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}";
        var courseEntity = createCourseEntity();
        var newCourseName = "TDD out of the box!";
        var description = "It will start soon, register right now!";
        var request = new UpdateCourseRequest();
        request.setName(newCourseName);
        request.setDescription(description);
        mockMvc.perform(put("/v1/courses/{id}", courseEntity.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_not_update_course_when_id_not_belongs_to_any_course() throws Exception {
        var expectedResult ="{\"error_message\":\"Course with id 0 not found.\"}";
        var newCourseName = "TDD out of the box!";
        var description = "It will start soon, register right now!";
        var request = new UpdateCourseRequest();
        request.setName(newCourseName);
        request.setDescription(description);
        mockMvc.perform(put("/v1/courses/{id}", 0L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_delete_course() throws Exception {
        var courseEntity = createCourseEntity();
        mockMvc.perform(delete("/v1/courses/{id}", courseEntity.getId())).andExpect(status().isNoContent());
        Assertions.assertTrue(
                courseEntityRepository.findById(courseEntity.getId()).isEmpty());
    }

    @Test
    @DirtiesContext
    void should_not_delete_course_with_students() throws Exception {
        var expectedResult = "{\"error_message\":\"Course has students and can't be be deleted.\"}";
        var courseEntity = createCourseEntity();
        createStudentEntityWithCourseEntity(courseEntity);
        mockMvc.perform(delete("/v1/courses/{id}", courseEntity.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_course_by_id() throws Exception {
        var expectedResult =
                "{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"},\"courses\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}";
        var courseEntity = createCourseEntity();
        mockMvc.perform(get("/v1/courses/{id}", courseEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_courses() throws Exception {
        createCourseEntity();
        var expectedResult =
                "{\"_embedded\":{\"courses\":[{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"},\"courses\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}";
        mockMvc.perform(get("/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_courses_detailed() throws Exception {
        var expectedResult =
                "{\"_embedded\":{\"courses\":[{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"students\":[{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\"}],\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"},\"courses\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}";
        CourseEntity courseEntity = createCourseEntity();
        createStudentEntityWithCourseEntity(courseEntity);
        mockMvc.perform(get("/v1/courses?detailed=true"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_courses_for_specific_student() throws Exception {
        var expectedResult =
                "{\"_embedded\":{\"courses\":[{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"students\":[{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\"}],\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"},\"courses\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/students/1\"}}}";
        CourseEntity courseEntity = createCourseEntity();
        StudentEntity studentEntity = createStudentEntityWithCourseEntity(courseEntity);
        mockMvc.perform(get("/v1/courses/students/{student_id}", studentEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_courses_without_students() throws Exception {
        var expectedResult =
                "{\"_embedded\":{\"courses\":[{\"id\":2,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/2\"},\"courses\":{\"href\":\"http://localhost/v1/courses?detailed=false\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/without-students\"}}}";
        CourseEntity courseEntity = createCourseEntity();
        createStudentEntityWithCourseEntity(courseEntity);
        createCourseEntity();
        mockMvc.perform(get("/v1/courses/without-students"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    private CourseEntity createCourseEntity() {
        var name = "TDD Course";
        var description = "It will start soon, register right now!";
        var courseEntity = new CourseEntity();
        courseEntity.setName(name);
        courseEntity.setDescription(description);
        courseEntityRepository.save(courseEntity);
        return courseEntity;
    }

    private StudentEntity createStudentEntityWithCourseEntity(CourseEntity courseEntity) {
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(identificationDocument);
        studentEntity.setCourseEntities(Set.of(courseEntity));
        studentEntityRepository.save(studentEntity);
        return studentEntity;
    }
}
