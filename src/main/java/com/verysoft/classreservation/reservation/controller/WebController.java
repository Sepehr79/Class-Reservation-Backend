package com.verysoft.classreservation.reservation.controller;

import com.verysoft.classreservation.reservation.dto.ExceptionDto;
import com.verysoft.classreservation.reservation.dto.UserDto;
import com.verysoft.classreservation.reservation.entity.UserEntity;
import com.verysoft.classreservation.reservation.service.CookieService;
import com.verysoft.classreservation.reservation.service.JwtService;
import com.verysoft.classreservation.reservation.service.UserEntityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WebController {

    private final UserEntityService userEntityService;

    private final JwtService jwtService;

    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDto userDto, HttpServletResponse response) {
        try {
            UserEntity userEntity = (UserEntity) userEntityService.loadUserByUsername(userDto.getUsername());
            if (userEntityService.validateUser(userDto, userEntity)) {
                String jwt = jwtService.createJwt(userEntity);
                response.addCookie(cookieService.createNewJwtCookie(jwt));
                return ResponseEntity.ok(new UserDto(userDto.getUsername(), userEntity.getRoles()));
            }
        } catch (UsernameNotFoundException ignored) {}
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new ExceptionDto("Incorrect username or password"));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserDto userDto) {
        try {
            userEntityService.createUser(userDto);
            return ResponseEntity.ok(new UserDto(userDto.getUsername(), userDto.getRoles()));
        } catch (DuplicateKeyException exception) {
            return ResponseEntity.badRequest().body(
                    new ExceptionDto("Duplicate username: " + userDto.getUsername())
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        response.addCookie(cookieService.createDeletingJwtCookie());
        return ResponseEntity.ok(Map.of("Message", "Logout success"));
    }

    @GetMapping("/allRoles")
    public String secureEndpoint(Authentication authentication) {
        return "Hello " + authentication.getName();
    }

    @GetMapping("/managers")
    public String managers(Authentication authentication) {
        return "Hello, " + authentication.getAuthorities();
    }

    @GetMapping("/teachers")
    public String teachers(Authentication authentication) {
        return "Hello, " + authentication.getAuthorities();
    }
    @GetMapping("/students")
    public String students(Authentication authentication) {
        return "Hello, " + authentication.getAuthorities();
    }


}
