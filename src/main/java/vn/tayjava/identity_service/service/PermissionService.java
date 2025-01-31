package vn.tayjava.identity_service.service;

import vn.tayjava.identity_service.dto.request.PermissionRequest;
import vn.tayjava.identity_service.dto.respone.PermissionResponse;
import vn.tayjava.identity_service.entity.Permission;
import vn.tayjava.identity_service.mapper.PermissionMapper;
import vn.tayjava.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    public PermissionResponse create(PermissionRequest permissionRequest){
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }
    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permission
                -> permissionMapper.toPermissionResponse(permission)).toList();
    }
    public void delete(String permissionName){
        permissionRepository.deleteById(permissionName);
    }
}
