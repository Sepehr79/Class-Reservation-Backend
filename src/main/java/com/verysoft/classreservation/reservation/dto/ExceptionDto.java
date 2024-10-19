package com.verysoft.classreservation.reservation.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionDto {
    private String message;
    private LocalDateTime date;

    public ExceptionDto(String message) {
        this.message = message;
        this.date = LocalDateTime.now();
    }
}
