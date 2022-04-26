package io.metadata.schoolregistration.domain.usecase.course.read;

import io.metadata.schoolregistration.domain.entity.Course;
import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("findAllCoursesUseCase")
@RequiredArgsConstructor
public class FindAllUseCaseImpl implements FindAllUseCase {

    private final CourseGateway courseGateway;

    @Override
    public List<Course> execute(Boolean detailed) {
        return courseGateway.findAll(detailed);
    }
}
