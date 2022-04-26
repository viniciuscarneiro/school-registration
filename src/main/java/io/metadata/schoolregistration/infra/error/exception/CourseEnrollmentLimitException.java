package io.metadata.schoolregistration.infra.error.exception;

public class CourseEnrollmentLimitException extends BadRequestException {
    public CourseEnrollmentLimitException(String message) {
        super(message);
    }
}
