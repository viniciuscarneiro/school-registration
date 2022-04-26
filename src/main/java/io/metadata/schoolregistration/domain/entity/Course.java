package io.metadata.schoolregistration.domain.entity;

import java.util.Optional;
import java.util.Set;

public record Course(Optional<Long> id, String name, String description, Optional<Set<Student>> students) {}
