package io.metadata.schoolregistration.infra.error.exception;

public class EntityNotFoundException extends BadRequestException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
