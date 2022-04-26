package io.metadata.schoolregistration.infra.error.exception;

public class CourseWithEnrolledStudentsException extends BadRequestException {
    public CourseWithEnrolledStudentsException(String message) {
        super(message);
    }
}
