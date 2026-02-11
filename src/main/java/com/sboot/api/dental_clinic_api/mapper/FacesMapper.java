package com.sboot.api.dental_clinic_api.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class FacesMapper {

    @Named("stringToList")
    public List<String> stringToList(String faces) {
        if (faces == null || faces.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(faces.split(","));
    }

    @Named("listToString")
    public String listToString(List<String> faces) {
        if (faces == null || faces.isEmpty()) {
            return null;
        }
        return String.join(",", faces);
    }
}
