package vn.tayjava.identity_service.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     String username;
     String password;
     String firstName;
     String lastName;
     LocalDate dob;
     Set<RoleResponse> roles;
}
