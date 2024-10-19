package com.verysoft.classreservation.reservation.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class RoleConvertor implements AttributeConverter<List<String>, String> {

    private static final String JOINING_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> roles) {
        return roles == null ? "" : String.join(JOINING_CHAR, roles);
    }

    @Override
    public List<String> convertToEntityAttribute(String roles) {
        return new ArrayList<>(Arrays.asList(roles.split(JOINING_CHAR)));
    }
}
