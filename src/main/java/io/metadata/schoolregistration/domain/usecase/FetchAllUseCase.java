package io.metadata.schoolregistration.domain.usecase;

import java.util.List;

public interface FindAllUseCase<T> {
    List<T> execute();
}
