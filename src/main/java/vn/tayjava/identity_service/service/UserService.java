package vn.tayjava.identity_service.service;

import vn.tayjava.identity_service.dto.request.UserCreationRequest;
import vn.tayjava.identity_service.dto.request.UserUpdateRequest;
import vn.tayjava.identity_service.dto.respone.UserResponse;
import vn.tayjava.identity_service.entity.User;
import vn.tayjava.identity_service.enums.Role;
import vn.tayjava.identity_service.exception.AppException;
import vn.tayjava.identity_service.exception.ErrorCode;
import vn.tayjava.identity_service.mapper.UserMapper;
import vn.tayjava.identity_service.repository.RoleRepository;
import vn.tayjava.identity_service.repository.UserRepository;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode((request.getPassword())));
        return userMapper.toUserResponse(user);
    }

    public UserResponse  updateUser(UserUpdateRequest request, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    @PreAuthorize("hasRole('ADMIN')") // chặn role khác vào method này
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    public UserResponse  getMyInfor(){
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User u = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(u);
    }
    public UserResponse  getAUser(String id){
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
