package io.metadata.schoolregistration.infra.error.exception;

public class StudentEnrollmentLimitException extends BadRequestException {
    public StudentEnrollmentLimitException(String message) {
        super(message);
    }
}
