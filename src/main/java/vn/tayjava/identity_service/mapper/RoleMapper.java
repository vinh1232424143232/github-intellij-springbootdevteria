package vn.tayjava.identity_service.mapper;

import vn.tayjava.identity_service.dto.request.RoleRequest;
import vn.tayjava.identity_service.dto.respone.RoleResponse;
import vn.tayjava.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.stream.Collectors;


@Mapper(componentModel="spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);
    RoleResponse toRoleResponse(Role role);
}
