package io.metadata.schoolregistration.infra.resource.v1.student.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class UpdateStudentRequest {
    @NotBlank(message = "Name is mandatory")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "[A-Za-z\\d._%-+]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,4}", message = "Invalid email")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @JsonProperty("phone_number")
    private String phoneNumber;
}
