package io.metadata.schoolregistration.infra.adpter;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.adapter.student.StudentDataGateway;
import io.metadata.schoolregistration.infra.adapter.student.StudentMapper;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import io.metadata.schoolregistration.infra.error.exception.EntityNotFoundException;
import io.metadata.schoolregistration.infra.repository.StudentEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentGatewayTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentEntityRepository studentEntityRepository;

    @InjectMocks
    private StudentDataGateway studentGateway;

    @Test
    void should_persist() {
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentToBePersisted =
                new Student(Optional.empty(), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        var studentEntityToBePersisted = new StudentEntity();
        studentEntityToBePersisted.setFullName(fullName);
        studentEntityToBePersisted.setEmail(email);
        studentEntityToBePersisted.setPhoneNumber(phoneNumber);
        studentEntityToBePersisted.setIdentificationDocument(identificationDocument);
        when(studentMapper.toEntity(studentToBePersisted)).thenReturn(studentEntityToBePersisted);
        var persistedStudentEntityId = 1L;
        var persistedStudentEntity = new StudentEntity();
        persistedStudentEntity.setId(persistedStudentEntityId);
        persistedStudentEntity.setFullName(fullName);
        persistedStudentEntity.setEmail(email);
        persistedStudentEntity.setPhoneNumber(phoneNumber);
        persistedStudentEntity.setIdentificationDocument(identificationDocument);
        when(studentEntityRepository.save(studentEntityToBePersisted)).thenReturn(persistedStudentEntity);
        var persistedStudent = new Student(
                Optional.of(persistedStudentEntityId),
                fullName,
                email,
                phoneNumber,
                identificationDocument,
                Optional.empty());
        when(studentMapper.toDomain(persistedStudentEntity, Boolean.FALSE)).thenReturn(persistedStudent);
        var result = studentGateway.persist(studentToBePersisted, Boolean.FALSE);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.id().isEmpty()),
                () -> assertEquals(persistedStudentEntityId, result.id().orElseThrow()),
                () -> assertEquals(fullName, result.fullName()),
                () -> assertEquals(email, result.email()),
                () -> assertEquals(phoneNumber, result.phoneNumber()),
                () -> assertEquals(identificationDocument, result.identificationDocument()),
                () -> assertTrue(result.courses().isEmpty()));
        verify(studentMapper, times(1)).toEntity(studentToBePersisted);
        verify(studentEntityRepository, times(1)).save(studentEntityToBePersisted);
        verify(studentMapper, times(1)).toDomain(persistedStudentEntity, Boolean.FALSE);
    }

    @Test
    void should_delete() {
        var studentId = 1L;
        doNothing().when(studentEntityRepository).deleteById(studentId);
        studentGateway.delete(studentId);
        verifyNoInteractions(studentMapper);
        verify(studentEntityRepository, times(1)).deleteById(studentId);
    }

    @Test
    void should_throws_entity_not_found_when_the_given_id_not_belongs_to_any_entity() {
        var studentId = 0L;
        when(studentEntityRepository.findById(studentId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> studentGateway.findById(studentId, Boolean.FALSE));
        verify(studentEntityRepository, times(1)).findById(studentId);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void should_return_summarized_entity_when_given_id_belongs_to_any_entity() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(identificationDocument);
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        when(studentMapper.toDomain(studentEntity, Boolean.FALSE)).thenReturn(student);
        when(studentEntityRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        var result = studentGateway.findById(studentId, Boolean.FALSE);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(studentEntity.getId(), result.id().orElseThrow()),
                () -> assertEquals(studentEntity.getFullName(), result.fullName()),
                () -> assertEquals(studentEntity.getEmail(), result.email()),
                () -> assertEquals(studentEntity.getPhoneNumber(), result.phoneNumber()),
                () -> assertEquals(studentEntity.getIdentificationDocument(), result.identificationDocument()),
                () -> assertTrue(result.courses().isEmpty()));
        verify(studentEntityRepository, times(1)).findById(studentId);
        verify(studentMapper, times(1)).toDomain(studentEntity, Boolean.FALSE);
    }

    @Test
    void should_return_detailed_entity_when_given_id_belongs_to_any_entity() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(identificationDocument);
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        var courseEntities = Set.of(courseEntity);
        studentEntity.setCourseEntities(courseEntities);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        var courses = Set.of(course);
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.of(courses));
        when(studentMapper.toDomain(studentEntity, Boolean.TRUE)).thenReturn(student);
        when(studentEntityRepository.findDetailedById(studentId)).thenReturn(Optional.of(studentEntity));
        var result = studentGateway.findById(studentId, Boolean.TRUE);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(studentEntity.getId(), result.id().orElseThrow()),
                () -> assertEquals(studentEntity.getFullName(), result.fullName()),
                () -> assertEquals(studentEntity.getEmail(), result.email()),
                () -> assertEquals(studentEntity.getPhoneNumber(), result.phoneNumber()),
                () -> assertEquals(studentEntity.getIdentificationDocument(), result.identificationDocument()),
                () -> assertTrue(result.courses().isPresent()),
                () -> {
                    var coursesResult = result.courses().stream().findAny().orElseThrow();
                    assertEquals(courseEntities.size(), coursesResult.size());
                    var courseResult = coursesResult.stream().findAny().orElseThrow();
                    assertEquals(course, courseResult);
                    assertEquals(courseId, courseResult.id().orElseThrow());
                    assertEquals(courseName, courseResult.name());
                    assertEquals(courseDescription, courseResult.description());
                    assertTrue(courseResult.students().isEmpty());
                });
        verify(studentEntityRepository, times(1)).findDetailedById(studentId);
        verify(studentMapper, times(1)).toDomain(studentEntity, Boolean.TRUE);
    }

    @Test
    void should_return_all_summarized() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(identificationDocument);
        var studentEntityList = List.of(studentEntity);
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        when(studentMapper.toDomain(studentEntity, Boolean.FALSE)).thenReturn(student);
        when(studentEntityRepository.findAll()).thenReturn(studentEntityList);
        var result = studentGateway.findAll(Boolean.FALSE);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(studentEntityList.size(), result.size()),
                () -> {
                    var studentResult = result.stream().findAny().orElseThrow();
                    assertEquals(student, studentResult);
                    assertEquals(studentEntity.getId(), studentResult.id().orElseThrow());
                    assertEquals(studentEntity.getFullName(), studentResult.fullName());
                    assertEquals(studentEntity.getPhoneNumber(), studentResult.phoneNumber());
                    assertEquals(studentEntity.getIdentificationDocument(), studentResult.identificationDocument());
                    assertTrue(studentResult.courses().isEmpty());
                });
        verify(studentEntityRepository, times(1)).findAll();
        verify(studentEntityRepository, times(0)).findAllBy();
        verify(studentMapper, times(1)).toDomain(studentEntity, Boolean.FALSE);
    }

    @Test
    void should_return_all_detailed() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(identificationDocument);
        var studentEntityList = List.of(studentEntity);
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        var courseEntities = Set.of(courseEntity);
        studentEntity.setCourseEntities(courseEntities);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        var courses = Set.of(course);
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.of(courses));
        when(studentMapper.toDomain(studentEntity, Boolean.TRUE)).thenReturn(student);
        when(studentEntityRepository.findAllBy()).thenReturn(studentEntityList);
        var result = studentGateway.findAll(Boolean.TRUE);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(studentEntityList.size(), result.size()),
                () -> {
                    var studentResult = result.stream().findAny().orElseThrow();
                    assertEquals(student, studentResult);
                    assertEquals(studentEntity.getId(), studentResult.id().orElseThrow());
                    assertEquals(studentEntity.getFullName(), studentResult.fullName());
                    assertEquals(studentEntity.getPhoneNumber(), studentResult.phoneNumber());
                    assertEquals(studentEntity.getIdentificationDocument(), studentResult.identificationDocument());
                    assertFalse(studentResult.courses().isEmpty());
                    var coursesResult =
                            studentResult.courses().stream().findAny().orElseThrow();
                    assertFalse(coursesResult.isEmpty());
                    var courseResult = coursesResult.stream().findAny().orElseThrow();
                    assertEquals(course, courseResult);
                    assertEquals(courseId, courseResult.id().orElseThrow());
                    assertEquals(courseName, courseResult.name());
                    assertEquals(courseDescription, courseResult.description());
                    assertFalse(courseResult.students().isPresent());
                });
        verify(studentEntityRepository, times(0)).findAll();
        verify(studentEntityRepository, times(1)).findAllBy();
        verify(studentMapper, times(1)).toDomain(studentEntity, Boolean.TRUE);
    }

    @Test
    void should_return_all_without_courses() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(identificationDocument);
        var studentEntityList = List.of(studentEntity);
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.empty());
        when(studentMapper.toDomain(studentEntity, Boolean.FALSE)).thenReturn(student);
        when(studentEntityRepository.findByCourseEntitiesIsNull()).thenReturn(studentEntityList);
        var result = studentGateway.findAllWithoutCourses();
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(studentEntityList.size(), result.size()),
                () -> {
                    var studentResult = result.stream().findAny().orElseThrow();
                    assertEquals(student, studentResult);
                    assertEquals(studentEntity.getId(), studentResult.id().orElseThrow());
                    assertEquals(studentEntity.getFullName(), studentResult.fullName());
                    assertEquals(studentEntity.getPhoneNumber(), studentResult.phoneNumber());
                    assertEquals(studentEntity.getIdentificationDocument(), studentResult.identificationDocument());
                    assertTrue(studentResult.courses().isEmpty());
                });
        verify(studentEntityRepository, times(1)).findByCourseEntitiesIsNull();
        verify(studentMapper, times(1)).toDomain(studentEntity, Boolean.FALSE);
    }

    @Test
    void should_return_true_when_given_id_belongs_to_any_entity() {
        var studentId = 1L;
        when(studentEntityRepository.existsById(studentId)).thenReturn(Boolean.TRUE);
        var result = studentGateway.existsById(studentId);
        assertTrue(result);
        verify(studentEntityRepository, times(1)).existsById(studentId);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void should_return_false_when_given_id_not_belongs_to_any_entity() {
        var studentId = 0L;
        when(studentEntityRepository.existsById(studentId)).thenReturn(Boolean.FALSE);
        var result = studentGateway.existsById(studentId);
        assertFalse(result);
        verify(studentEntityRepository, times(1)).existsById(studentId);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void should_return_true_when_given_identification_document_belongs_to_any_student() {
        var identificationDocument = "123456";
        when(studentEntityRepository.existsByIdentificationDocument(identificationDocument)).thenReturn(Boolean.TRUE);
        var result = studentGateway.existsByIdentificationDocument(identificationDocument);
        assertTrue(result);
        verify(studentEntityRepository, times(1)).existsByIdentificationDocument(identificationDocument);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void should_return_false_when_given_identification_document_not_belongs_to_any_student() {
        var identificationDocument = "123456";
        when(studentEntityRepository.existsByIdentificationDocument(identificationDocument)).thenReturn(Boolean.FALSE);
        var result = studentGateway.existsByIdentificationDocument(identificationDocument);
        assertFalse(result);
        verify(studentEntityRepository, times(1)).existsByIdentificationDocument(identificationDocument);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void should_return_all_students_enrolled_in_given_course() {
        var studentId = 1L;
        var fullName = "Full name";
        var email = "email@email.com";
        var phoneNumber = "+145896875";
        var identificationDocument = "78482399478";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(fullName);
        studentEntity.setEmail(email);
        studentEntity.setPhoneNumber(phoneNumber);
        studentEntity.setIdentificationDocument(identificationDocument);
        var studentEntityList = List.of(studentEntity);
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        var courseEntities = Set.of(courseEntity);
        studentEntity.setCourseEntities(courseEntities);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        var courses = Set.of(course);
        var student = new Student(
                Optional.of(studentId), fullName, email, phoneNumber, identificationDocument, Optional.of(courses));
        when(studentMapper.toDomain(studentEntity, Boolean.TRUE)).thenReturn(student);
        when(studentEntityRepository.findByCourseEntitiesId(courseId)).thenReturn(studentEntityList);
        var result = studentGateway.findAllWithSpecificCourse(courseId);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(studentEntityList.size(), result.size()),
                () -> {
                    var studentResult = result.stream().findAny().orElseThrow();
                    assertEquals(student, studentResult);
                    assertEquals(studentEntity.getId(), studentResult.id().orElseThrow());
                    assertEquals(studentEntity.getFullName(), studentResult.fullName());
                    assertEquals(studentEntity.getPhoneNumber(), studentResult.phoneNumber());
                    assertEquals(studentEntity.getIdentificationDocument(), studentResult.identificationDocument());
                    assertFalse(studentResult.courses().isEmpty());
                    var coursesResult =
                            studentResult.courses().stream().findAny().orElseThrow();
                    assertFalse(coursesResult.isEmpty());
                    var courseResult = coursesResult.stream().findAny().orElseThrow();
                    assertEquals(course, courseResult);
                    assertEquals(courseId, courseResult.id().orElseThrow());
                    assertEquals(courseName, courseResult.name());
                    assertEquals(courseDescription, courseResult.description());
                    assertFalse(courseResult.students().isPresent());
                });
        verify(studentEntityRepository, times(1)).findByCourseEntitiesId(courseId);
        verify(studentMapper, times(1)).toDomain(studentEntity, Boolean.TRUE);
    }
}
