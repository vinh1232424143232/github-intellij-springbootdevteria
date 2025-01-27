package vn.tayjava.identity_service.controller;

import vn.tayjava.identity_service.dto.request.ApiResponse;
import vn.tayjava.identity_service.dto.request.UserCreationRequest;
import vn.tayjava.identity_service.dto.request.UserUpdateRequest;
import vn.tayjava.identity_service.dto.respone.UserResponse;
import vn.tayjava.identity_service.entity.User;
import vn.tayjava.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiRespone = new ApiResponse<>();
        apiRespone.setResult(userService.createUser(request));
        return apiRespone;
    }
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication =  SecurityContextHolder.getContext().getAuthentication();
        log.info("user name: {}", authentication.getName());
        authentication.getAuthorities().stream().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                        .result(userService.getAllUsers())
                        .build();
    }
    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable String userId){
        return userService.getAUser(userId);
    }
    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId,@RequestBody UserUpdateRequest request){
        return userService.updateUser( request, userId);
    }
    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable String userId){
        this.userService.deleteUser(userId);
    }
    @GetMapping("/myinfor")
    ApiResponse<UserResponse> getMyInfor(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfor())
                .build();
    }

}
