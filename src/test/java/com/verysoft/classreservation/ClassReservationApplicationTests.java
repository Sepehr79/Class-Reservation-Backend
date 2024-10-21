package com.verysoft.classreservation;

import com.verysoft.classreservation.reservation.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClassReservationApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void jwtTokenAuthenticationTest() {
        ResponseEntity<String> protectedResponse1 = testRestTemplate.getForEntity("http://localhost:" + port + "/managers", String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, protectedResponse1.getStatusCode());

        ResponseEntity<UserDto> signupResponse = testRestTemplate.postForEntity("http://localhost:" + port + "/signup",
                new UserDto("sepehr", "1234", List.of("MANAGER")), UserDto.class);
        Assertions.assertEquals(HttpStatus.OK, signupResponse.getStatusCode());
        Assertions.assertEquals("sepehr", signupResponse.getBody().getUsername());
        Assertions.assertEquals("MANAGER", signupResponse.getBody().getRoles().get(0));

        ResponseEntity<UserDto> loginResponse = testRestTemplate.postForEntity("http://localhost:" + port + "/login" ,
                new UserDto("sepehr", "1234", List.of()), UserDto.class);
        Assertions.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        String jwtCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assert jwtCookie != null;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", jwtCookie);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        ResponseEntity<String> protectedResponse2 = testRestTemplate.exchange(
                "http://localhost:" + port + "/managers",
                HttpMethod.GET,
                requestEntity,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, protectedResponse2.getStatusCode());


    }

}
