package io.metadata.schoolregistration.adpter;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.entity.Student;
import io.metadata.schoolregistration.infra.adapter.course.CourseDataGateway;
import io.metadata.schoolregistration.infra.adapter.course.CourseMapper;
import io.metadata.schoolregistration.infra.entitymodel.CourseEntity;
import io.metadata.schoolregistration.infra.entitymodel.StudentEntity;
import io.metadata.schoolregistration.infra.error.exception.EntityNotFoundException;
import io.metadata.schoolregistration.infra.repository.CourseEntityRepository;
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
class CourseDataGatewayTest {

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseEntityRepository courseEntityRepository;

    @InjectMocks
    private CourseDataGateway courseGateway;

    @Test
    void should_throws_entity_not_found_when_the_given_id_not_belongs_to_any_entity() {
        var courseId = 0L;
        when(courseEntityRepository.findById(courseId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> courseGateway.findById(courseId));
        verify(courseEntityRepository, times(1)).findById(courseId);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void should_return_entity_when_given_id_belongs_to_any_entity() {
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        when(courseMapper.toDomain(courseEntity, Boolean.FALSE)).thenReturn(course);
        when(courseEntityRepository.findById(courseId)).thenReturn(Optional.of(courseEntity));
        var result = courseGateway.findById(courseId);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(courseEntity.getId(), result.id().orElseThrow()),
                () -> assertEquals(courseEntity.getName(), result.name()),
                () -> assertEquals(courseEntity.getDescription(), result.description()),
                () -> assertTrue(result.students().isEmpty()));
        verify(courseEntityRepository, times(1)).findById(courseId);
        verify(courseMapper, times(1)).toDomain(courseEntity, Boolean.FALSE);
    }

    @Test
    void should_return_false_when_given_id_not_belongs_to_any_entity() {
        var courseId = 0L;
        when(courseEntityRepository.existsById(courseId)).thenReturn(Boolean.FALSE);
        var result = courseGateway.existsById(courseId);
        assertFalse(result);
        verify(courseEntityRepository, times(1)).existsById(courseId);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void should_return_true_when_given_id_belongs_to_any_entity() {
        var courseId = 1L;
        when(courseEntityRepository.existsById(courseId)).thenReturn(Boolean.TRUE);
        var result = courseGateway.existsById(courseId);
        assertTrue(result);
        verify(courseEntityRepository, times(1)).existsById(courseId);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void should_return_all_summarized() {
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        var courseEntityList = List.of(courseEntity);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        when(courseMapper.toDomain(courseEntity, Boolean.FALSE)).thenReturn(course);
        when(courseEntityRepository.findAll()).thenReturn(courseEntityList);
        var result = courseGateway.findAll(Boolean.FALSE);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(courseEntityList.size(), result.size()),
                () -> {
                    var courseResult = result.stream().findAny().orElseThrow();
                    assertEquals(course, courseResult);
                    assertEquals(courseId, courseResult.id().orElseThrow());
                    assertEquals(courseName, courseResult.name());
                    assertEquals(courseDescription, courseResult.description());
                    assertTrue(courseResult.students().isEmpty());
                });
        verify(courseEntityRepository, times(1)).findAll();
        verify(courseEntityRepository, times(0)).findAllBy();
        verify(courseMapper, times(1)).toDomain(courseEntity, Boolean.FALSE);
    }

    @Test
    void should_return_all_detailed() {
        var studentId = 2L;
        var studentFullName = "Full name";
        var studentPhoneNumber = "+5534999999999";
        var studentIdentificationDocument = "78945612314";
        var studentEmail = "123@abc.com";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(studentFullName);
        studentEntity.setPhoneNumber(studentPhoneNumber);
        studentEntity.setIdentificationDocument(studentIdentificationDocument);
        studentEntity.setEmail(studentEmail);
        var studentEntities = Set.of(studentEntity);
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        courseEntity.setStudentEntities(studentEntities);
        var courseEntityList = List.of(courseEntity);
        var student = new Student(
                Optional.of(studentId),
                studentFullName,
                studentEmail,
                studentPhoneNumber,
                studentIdentificationDocument,
                Optional.empty());
        var students = Set.of(student);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.of(students));
        when(courseMapper.toDomain(courseEntity, Boolean.TRUE)).thenReturn(course);
        when(courseEntityRepository.findAllBy()).thenReturn(courseEntityList);
        var result = courseGateway.findAll(Boolean.TRUE);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(courseEntityList.size(), result.size()),
                () -> {
                    var courseResult = result.stream().findAny().orElseThrow();
                    assertEquals(course, courseResult);
                    assertEquals(courseId, courseResult.id().orElseThrow());
                    assertEquals(courseName, courseResult.name());
                    assertEquals(courseDescription, courseResult.description());
                    assertFalse(courseResult.students().isEmpty());
                    var studentsResult =
                            courseResult.students().stream().findAny().orElseThrow();
                    assertEquals(studentEntities.size(), studentsResult.size());
                    var studentResult = studentsResult.stream().findAny().orElseThrow();
                    assertEquals(student, studentResult);
                    assertEquals(studentId, studentResult.id().orElseThrow());
                    assertEquals(studentFullName, studentResult.fullName());
                    assertEquals(studentPhoneNumber, studentResult.phoneNumber());
                    assertEquals(studentIdentificationDocument, studentResult.identificationDocument());
                    assertTrue(studentResult.courses().isEmpty());
                });
        verify(courseEntityRepository, times(1)).findAllBy();
        verify(courseEntityRepository, times(0)).findAll();
        verify(courseMapper, times(1)).toDomain(courseEntity, Boolean.TRUE);
    }

    @Test
    void should_return_all_for_specific_student() {
        var studentId = 2L;
        var studentFullName = "Full name";
        var studentPhoneNumber = "+5534999999999";
        var studentIdentificationDocument = "78945612314";
        var studentEmail = "123@abc.com";
        var studentEntity = new StudentEntity();
        studentEntity.setId(studentId);
        studentEntity.setFullName(studentFullName);
        studentEntity.setPhoneNumber(studentPhoneNumber);
        studentEntity.setIdentificationDocument(studentIdentificationDocument);
        studentEntity.setEmail(studentEmail);
        var studentEntities = Set.of(studentEntity);
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        courseEntity.setStudentEntities(studentEntities);
        var courseEntityList = List.of(courseEntity);
        var student = new Student(
                Optional.of(studentId),
                studentFullName,
                studentEmail,
                studentPhoneNumber,
                studentIdentificationDocument,
                Optional.empty());
        var students = Set.of(student);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.of(students));
        when(courseMapper.toDomain(courseEntity, Boolean.TRUE)).thenReturn(course);
        when(courseEntityRepository.findByStudentEntitiesId(studentId)).thenReturn(courseEntityList);
        var result = courseGateway.findAllForSpecificStudent(studentId);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(courseEntityList.size(), result.size()),
                () -> {
                    var courseResult = result.stream().findAny().orElseThrow();
                    assertEquals(course, courseResult);
                    assertEquals(courseId, courseResult.id().orElseThrow());
                    assertEquals(courseName, courseResult.name());
                    assertEquals(courseDescription, courseResult.description());
                    assertFalse(courseResult.students().isEmpty());
                    var studentsResult =
                            courseResult.students().stream().findAny().orElseThrow();
                    assertEquals(studentEntities.size(), studentsResult.size());
                    var studentResult = studentsResult.stream().findAny().orElseThrow();
                    assertEquals(student, studentResult);
                    assertEquals(studentId, studentResult.id().orElseThrow());
                    assertEquals(studentFullName, studentResult.fullName());
                    assertEquals(studentPhoneNumber, studentResult.phoneNumber());
                    assertEquals(studentIdentificationDocument, studentResult.identificationDocument());
                    assertTrue(studentResult.courses().isEmpty());
                });
        verify(courseEntityRepository, times(1)).findByStudentEntitiesId(studentId);
        verify(courseMapper, times(1)).toDomain(courseEntity, Boolean.TRUE);
    }

    @Test
    void should_return_all_without_students() {
        var courseId = 1L;
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseEntity = new CourseEntity();
        courseEntity.setId(courseId);
        courseEntity.setName(courseName);
        courseEntity.setDescription(courseDescription);
        var courseEntityList = List.of(courseEntity);
        var course = new Course(Optional.of(courseId), courseName, courseDescription, Optional.empty());
        when(courseMapper.toDomain(courseEntity, Boolean.FALSE)).thenReturn(course);
        when(courseEntityRepository.findByStudentEntitiesIsNull()).thenReturn(courseEntityList);
        var result = courseGateway.findAllWithoutStudents();
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(courseEntityList.size(), result.size()),
                () -> {
                    var courseResult = result.stream().findAny().orElseThrow();
                    assertEquals(course, courseResult);
                    assertEquals(courseId, courseResult.id().orElseThrow());
                    assertEquals(courseName, courseResult.name());
                    assertEquals(courseDescription, courseResult.description());
                    assertTrue(courseResult.students().isEmpty());
                });
        verify(courseEntityRepository, times(1)).findByStudentEntitiesIsNull();
        verify(courseMapper, times(1)).toDomain(courseEntity, Boolean.FALSE);
    }

    @Test
    void should_persist() {
        var courseName = "Course name";
        var courseDescription = "Course description";
        var courseToBePersisted = new Course(Optional.empty(), courseName, courseDescription, Optional.empty());
        var courseEntityToBePersisted = new CourseEntity();
        courseEntityToBePersisted.setName(courseName);
        courseEntityToBePersisted.setDescription(courseDescription);
        when(courseMapper.toEntity(courseToBePersisted)).thenReturn(courseEntityToBePersisted);
        var persistedCourseEntityId = 1L;
        var persistedCourseEntity = new CourseEntity();
        persistedCourseEntity.setId(persistedCourseEntityId);
        persistedCourseEntity.setName(courseName);
        persistedCourseEntity.setDescription(courseDescription);
        when(courseEntityRepository.save(courseEntityToBePersisted)).thenReturn(persistedCourseEntity);
        var persistedCourse =
                new Course(Optional.of(persistedCourseEntityId), courseName, courseDescription, Optional.empty());
        when(courseMapper.toDomain(persistedCourseEntity, Boolean.FALSE)).thenReturn(persistedCourse);
        var result = courseGateway.persist(courseToBePersisted);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.id().isEmpty()),
                () -> assertEquals(persistedCourseEntityId, result.id().orElseThrow()),
                () -> assertEquals(courseName, result.name()),
                () -> assertEquals(courseDescription, result.description()),
                () -> assertTrue(result.students().isEmpty()));
        verify(courseMapper, times(1)).toEntity(courseToBePersisted);
        verify(courseEntityRepository, times(1)).save(courseEntityToBePersisted);
        verify(courseMapper, times(1)).toDomain(persistedCourseEntity, Boolean.FALSE);
    }

    @Test
    void should_delete() {
        var courseId = 1L;
        doNothing().when(courseEntityRepository).deleteById(courseId);
        courseGateway.delete(courseId);
        verifyNoInteractions(courseMapper);
        verify(courseEntityRepository, times(1)).deleteById(courseId);
    }
}
