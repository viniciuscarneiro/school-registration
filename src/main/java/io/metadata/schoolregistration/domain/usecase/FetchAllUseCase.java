package io.metadata.schoolregistration.domain.usecase;

import java.util.List;

public interface FetchAllUseCase<T> {
    List<T> execute();
}
