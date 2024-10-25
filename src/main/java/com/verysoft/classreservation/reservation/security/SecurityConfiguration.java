package com.verysoft.classreservation.reservation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final HttpCookieFilter requestFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
         return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                 .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/", "/login", "/signup", "/swagger-ui/**", "/v3/api-docs/**")
                            .permitAll();
                    request.requestMatchers("/managers", "/managers/**").hasRole("MANAGER");
                    request.requestMatchers("/students", "/students/**").hasRole("STUDENT");
                    request.requestMatchers("/teachers", "/teachers/**").hasRole("TEACHER");
                    request.anyRequest().authenticated();
                })
                .sessionManagement(sessionPolicy -> sessionPolicy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .addFilterAt(requestFilter, BasicAuthenticationFilter.class)
                 .formLogin(AbstractHttpConfigurer::disable)
                 .logout(AbstractHttpConfigurer::disable)
                 .build();
    }

}
