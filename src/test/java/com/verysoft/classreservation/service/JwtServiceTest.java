package com.verysoft.classreservation.service;

import com.verysoft.classreservation.reservation.entity.UserEntity;
import com.verysoft.classreservation.reservation.service.JwtService;
import io.fusionauth.jwt.JWTException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;

public class JwtServiceTest {

    private static final JwtService JWT_SERVICE = new JwtService();

    @Test
    void generateAndVerifyJwt() {
        ReflectionTestUtils.setField(JWT_SERVICE, "cookieDuration", Duration.ofSeconds(10));
        UserEntity userEntity = new UserEntity("sepehr", "11234", List.of("MANAGER"));
        String jwt = JWT_SERVICE.createJwt(userEntity);
        UserEntity user = JWT_SERVICE.verifyJwt(jwt);
        Assertions.assertEquals("sepehr", user.getUsername());
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals("MANAGER", user.getRoles().get(0));
        // Invalid JWT
        try {
            JWT_SERVICE.verifyJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
            Assertions.fail();
        } catch (JWTException ignore) {
        }
    }

}
