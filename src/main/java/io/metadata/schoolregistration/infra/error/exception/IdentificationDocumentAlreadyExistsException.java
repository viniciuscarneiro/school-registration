package io.metadata.schoolregistration.infra.error.exception;

public class IdentificationDocumentAlreadyExistsException extends BadRequestException {
    public IdentificationDocumentAlreadyExistsException(String identificationDocument) {
        super("Identification document %s already exists.".formatted(identificationDocument));
    }
}
