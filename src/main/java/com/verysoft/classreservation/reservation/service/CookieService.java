package com.verysoft.classreservation.reservation.service;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {

    @Value("${app.cookie.name}")
    private String cookieName;

    @Value("${app.cookie.duration}")
    private Duration cookieDuration;

    public Cookie createNewJwtCookie(String cookieValue) {
        Cookie jwtCookie = new Cookie(cookieName, cookieValue);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge((int) cookieDuration.toSeconds());
        return jwtCookie;
    }

    public Cookie createDeletingJwtCookie() {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        return cookie;
    }

}
