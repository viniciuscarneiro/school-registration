package io.metadata.schoolregistration.domain.usecase;

public interface FetchByIdUseCase<T> {
    T execute(Long id);
}
