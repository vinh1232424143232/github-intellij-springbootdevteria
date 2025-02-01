package vn.tayjava.identity_service.configuration;

import vn.tayjava.identity_service.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig{
    private final String[] PUBLIC_ENDPOINTS ={"/users","/auth/token","/auth/introspect"};
    @Value("${jwt.signerKey}")
    private String signerKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer ->
                                                                    jwtConfigurer.decoder(jwtDecoder())
                                                                            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                                           .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                                                                );
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        return httpSecurity.build();
    }
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtAuthenticationConverter;
    };
    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(),"HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                                                            .macAlgorithm(MacAlgorithm.HS512)
                                                            .build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
