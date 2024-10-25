package com.verysoft.classreservation.reservation.security;

import com.verysoft.classreservation.reservation.dto.UserAuthenticationDto;
import com.verysoft.classreservation.reservation.entity.UserEntity;
import com.verysoft.classreservation.reservation.service.CookieService;
import com.verysoft.classreservation.reservation.service.JwtService;
import io.fusionauth.jwt.JWTException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpCookieFilter implements Filter {

    private final CookieService cookieService;

    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Optional<Cookie> sCookie = cookieService.getJwtCookie(servletRequest);
        if (sCookie.isPresent()) {
            try {
                final String jwt = sCookie.get().getValue();
                UserEntity userEntity = jwtService.verifyJwt(jwt);
                UserAuthenticationDto userAuthenticationDto = new UserAuthenticationDto(userEntity);
                userAuthenticationDto.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(userAuthenticationDto);
            } catch (JWTException exception) {
                ((HttpServletResponse) servletResponse).setStatus(HttpStatus.FORBIDDEN.value());
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
