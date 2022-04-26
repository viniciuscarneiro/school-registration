package io.metadata.schoolregistration.domain.usecase.course.read;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import io.metadata.schoolregistration.domain.usecase.FindAllUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("findAllCoursesDetailedUseCase")
@RequiredArgsConstructor
public class FindAllDetailedUseCaseImpl implements FindAllUseCase<Course> {

    private final CourseGateway courseGateway;

    @Override
    public List<Course> execute() {
        return courseGateway.findAll(Boolean.TRUE);
    }
}
