package io.metadata.schoolregistration.domain.usecase.course.delete;

import io.metadata.schoolregistration.domain.gateway.CourseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("deleteCourseUseCase")
@RequiredArgsConstructor
public class DeleteUseCaseImpl implements DeleteUseCase {
    private final CourseGateway courseGateway;
    private final DeleteCourseRule deleteCourseRule;

    @Override
    public void execute(Long courseId) {
        deleteCourseRule.executeRule(courseId);
        courseGateway.delete(courseId);
    }
}
