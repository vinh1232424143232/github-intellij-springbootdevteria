package vn.tayjava.identity_service.service;

import vn.tayjava.identity_service.dto.request.RoleRequest;
import vn.tayjava.identity_service.dto.respone.RoleResponse;
import vn.tayjava.identity_service.mapper.PermissionMapper;
import vn.tayjava.identity_service.mapper.RoleMapper;
import vn.tayjava.identity_service.repository.PermissionRepository;
import vn.tayjava.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public RoleResponse create(RoleRequest roleRequest) {
        var role = roleMapper.toRole(roleRequest);
        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }
    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream()
                .map(role  -> roleMapper.toRoleResponse(role))
                .toList();
    }
    public void delete(String roleName){
        roleRepository.deleteById(roleName);
    }
}
