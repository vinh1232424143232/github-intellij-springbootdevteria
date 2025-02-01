package vn.tayjava.identity_service.mapper;

import vn.tayjava.identity_service.dto.request.PermissionRequest;
import vn.tayjava.identity_service.dto.respone.PermissionResponse;
import vn.tayjava.identity_service.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
