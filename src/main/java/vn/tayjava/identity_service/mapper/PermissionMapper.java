package vn.tayjava.identity_service.mapper;

import vn.tayjava.identity_service.dto.request.PermissionRequest;
import vn.tayjava.identity_service.dto.respone.PermissionResponse;
import vn.tayjava.identity_service.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PermissionMapper {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    Permission toPermission(PermissionRequest permissionRequest);
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    PermissionResponse toPermissionResponse(Permission permission);
}
