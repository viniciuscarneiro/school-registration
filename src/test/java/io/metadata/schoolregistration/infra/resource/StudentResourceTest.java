package io.metadata.schoolregistration.infra.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import io.metadata.schoolregistration.infra.repository.CourseEntityRepository;
import io.metadata.schoolregistration.infra.repository.StudentEntityRepository;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.CreateStudentRequest;
import io.metadata.schoolregistration.infra.resource.v1.student.model.request.UpdateStudentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentResourceTest {

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
    void should_create_student() throws Exception {
        var expectedResult =
                "{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\"}";
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var request = new CreateStudentRequest();
        request.setFullName(fullName);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setIdentificationDocument(identificationDocument);
        mockMvc.perform(post("/v1/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_not_create_student_when_identification_document_already_exists() throws Exception {
        createStudentEntity();
        var expectedResult = "{\"error_message\":\"Identification document 78482399478 already exists.\"}";
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var request = new CreateStudentRequest();
        request.setFullName(fullName);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setIdentificationDocument(identificationDocument);
        mockMvc.perform(post("/v1/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_not_create_student_without_full_name() throws Exception {
        var expectedResult = "{\"fullName\":\"Full name is mandatory\"}";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var request = new CreateStudentRequest();

        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setIdentificationDocument(identificationDocument);
        mockMvc.perform(post("/v1/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_update_student() throws Exception {
        var expectedResult =
                "{\"id\":1,\"email\":\"updated.email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Updated Full name\",\"phone_number\":\"+3445896875\",\"identification_document\":\"78482399478\"}";
        var studentEntity = createStudentEntity();
        var fullName = "Updated Full name";
        var email = "updated.email@email.com";
        var phoneNumber = "+3445896875";
        var request = new UpdateStudentRequest();
        request.setFullName(fullName);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        mockMvc.perform(put("/v1/students/{id}", studentEntity.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_delete_student() throws Exception {
        var studentEntity = createStudentEntity();
        studentEntityRepository.save(studentEntity);
        mockMvc.perform(delete("/v1/students/{id}", studentEntity.getId())).andExpect(status().isNoContent());
        Assertions.assertTrue(
                courseEntityRepository.findById(studentEntity.getId()).isEmpty());
    }

    @Test
    @DirtiesContext
    void should_not_delete_student_when_id_not_belongs_to_any_student() throws Exception {
        var expectedResult = "{\"error_message\":\"Student with id 0 not found.\"}";
        mockMvc.perform(delete("/v1/students/{id}", 0L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_student_by_id() throws Exception {
        var expectedResult =
                "{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\"}";
        var studentEntity = createStudentEntity();
        mockMvc.perform(get("/v1/students/{id}", studentEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_summarized_students() throws Exception {
        createStudentEntity();
        var expectedResult =
                "{\"_embedded\":{\"students\":[{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\"}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students?detailed=false\"}}}";
        mockMvc.perform(get("/v1/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_detailed_students() throws Exception {
        var expectedResult =
                "{\"_embedded\":{\"students\":[{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\",\"courses\":[{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"}}}]}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students?detailed=false\"}}}";
        createStudentEntity(createCourseEntity());
        mockMvc.perform(get("/v1/students?detailed=true"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_students_without_courses() throws Exception {
        var expectedResult =
                "{\"_embedded\":{\"students\":[{\"id\":2,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/2\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"9874563214\"}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/without-courses\"}}}";
        createStudentEntity(createCourseEntity());
        createStudentEntity("9874563214");
        mockMvc.perform(get("/v1/students/without-courses"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_register_student_to_course() throws Exception {
        var expectedResult =
                "{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\",\"courses\":[{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"}}}]}";
        var courserEntity = createCourseEntity();
        var studentEntity = createStudentEntity();

        mockMvc.perform(put("/v1/students/{id}/courses/{course_id}", studentEntity.getId(), courserEntity.getId()))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_return_all_students_for_specific_course() throws Exception {
        var expectedResult =
                "{\"_embedded\":{\"students\":[{\"id\":1,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/1\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"78482399478\",\"courses\":[{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"}}}]},{\"id\":2,\"email\":\"email@email.com\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/2\"},\"students\":{\"href\":\"http://localhost/v1/students?detailed=false\"}},\"full_name\":\"Full name\",\"phone_number\":\"+145896875\",\"identification_document\":\"1241512312125\",\"courses\":[{\"id\":1,\"name\":\"TDD Course\",\"description\":\"It will start soon, register right now!\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/courses/1\"}}}]}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/students/courses/1\"}}}";
        CourseEntity courseEntity = createCourseEntity();
        createStudentEntity(courseEntity);
        createStudentEntity("1241512312125", courseEntity);
        mockMvc.perform(get("/v1/students/courses/{course_id}", courseEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DirtiesContext
    void should_unregister_student_from_course() throws Exception {
        CourseEntity courseEntity = createCourseEntity();
        var studentEntity = createStudentEntity(courseEntity);
        studentEntityRepository.save(studentEntity);
        mockMvc.perform(delete("/v1/students/{id}/courses/{course_id}", studentEntity.getId(), courseEntity.getId()))
                .andExpect(status().isNoContent());
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

    private StudentEntity createStudentEntity() {
        return studentEntityRepository.save(createStudentEntity("78482399478"));
    }

    private StudentEntity createStudentEntity(String identificationDocument) {
        return createStudentEntity(identificationDocument, null);
    }

    private StudentEntity createStudentEntity(CourseEntity courseEntity) {
        return createStudentEntity(null, courseEntity);
    }

    private StudentEntity createStudentEntity(String identificationDocument, CourseEntity courseEntity) {
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(
                Optional.ofNullable(identificationDocument).orElse("78482399478"));
        Optional.ofNullable(courseEntity).ifPresent(course -> studentEntity.setCourseEntities(Set.of(course)));
        studentEntityRepository.save(studentEntity);
        return studentEntity;
    }
}
