package vn.tayjava.identity_service.service;

import vn.tayjava.identity_service.dto.request.AuthenticationRequest;
import vn.tayjava.identity_service.dto.request.IntrospectRequest;
import vn.tayjava.identity_service.dto.respone.AuthenticationResponse;
import vn.tayjava.identity_service.dto.respone.IntrospectResponse;
import vn.tayjava.identity_service.entity.User;
import vn.tayjava.identity_service.exception.AppException;
import vn.tayjava.identity_service.exception.ErrorCode;
import vn.tayjava.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private  String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var token = introspectRequest.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()); //tạo đối tượng JWSVerifier để xác minh chữ kí của JWT
        SignedJWT signedJWT = SignedJWT.parse(token); // chuyển chuỗi JWT thành đối tượng SignedJWT để truy cập các tp như Header, Payload và Signature
        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

         var  verified = signedJWT.verify(verifier); //kiểm tra chữ kí của token đảm bảo token ko bị thay đổi kể tu khi được kí
         return IntrospectResponse.builder()
                 .valid(verified && expTime.after(new Date()))
                 .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername()).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPassword()
                , user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }
    private String generateToken(User user){
        //để tạo token bằng thư vien nibus dùng JWSObject
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); //cần thuật toán HS512
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("duahau.com")
                .issueTime(new Date()) //thoi gian issue ra token
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() //thoi gian token het han
                ))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); //convert claimset to json
        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); //cần thuật toán để sign
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" "); // các scope trong oathu2 phân cách nhau = dấu cách
//        if(!CollectionUtils.isEmpty(user.getRoles())){
//            user.getRoles().forEach(role -> {stringJoiner.add(role);});
//        }
        return stringJoiner.toString();
    }
}
