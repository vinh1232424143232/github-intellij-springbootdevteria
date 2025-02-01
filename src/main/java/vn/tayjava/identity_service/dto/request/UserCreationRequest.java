package vn.tayjava.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @Size(min = 4, message = "INVALID_USERNAME")
     String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
     String password;
     String firstName;
     String lastName;
     LocalDate dob;
    List<String> roles;

}
