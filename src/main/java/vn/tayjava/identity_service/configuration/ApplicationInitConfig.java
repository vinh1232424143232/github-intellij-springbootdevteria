package vn.tayjava.identity_service.configuration;

import vn.tayjava.identity_service.entity.User;
import vn.tayjava.identity_service.enums.Role;
import vn.tayjava.identity_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

//tạo user admin mỗi khi start
@Slf4j
@Configuration
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
         if( userRepository.findByUsername("admin").isEmpty()){
             var roles = new HashSet<String>();
             roles.add(Role.ADMIN.name());

             User user = User.builder()
                         .username("admin")
                         .password(passwordEncoder.encode("admin"))
                         //.roles(roles)
                         .build();
             userRepository.save(user);
             log.warn("user admin has been created with default password:admin");
            };
        };
    }
}
