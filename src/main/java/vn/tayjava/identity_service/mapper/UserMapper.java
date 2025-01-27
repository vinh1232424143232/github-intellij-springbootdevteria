package vn.tayjava.identity_service.mapper;

import vn.tayjava.identity_service.dto.request.UserCreationRequest;
import vn.tayjava.identity_service.dto.request.UserUpdateRequest;
import vn.tayjava.identity_service.dto.respone.UserResponse;
import vn.tayjava.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel="spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
