package io.metadata.schoolregistration.infra.entitymodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@EqualsAndHashCode(of = "id")
@Data
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onPrePersist() {
        setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    @PreUpdate
    public void onPreUpdate() {
        setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    @PreRemove
    public void onPreRemove() {
        setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
