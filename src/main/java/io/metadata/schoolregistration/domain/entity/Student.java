package io.metadata.schoolregistration.domain.entity;

import java.util.Optional;
import java.util.Set;

public record Student(
        Optional<Long> id,
        String fullName,
        String email,
        String phoneNumber,
        String identificationDocument,
        Optional<Set<Course>> courses) {}
