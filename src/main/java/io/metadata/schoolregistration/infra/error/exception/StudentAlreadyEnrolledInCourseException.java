package io.metadata.schoolregistration.infra.error.exception;

public class StudentAlreadyEnrolledInCourseException extends BadRequestException {
    public StudentAlreadyEnrolledInCourseException(String message) {
        super(message);
    }
}
