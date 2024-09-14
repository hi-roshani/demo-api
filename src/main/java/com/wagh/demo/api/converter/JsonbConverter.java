package com.wagh.demo.api.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

@Converter(autoApply = false)
public class JsonbConverter implements AttributeConverter<Map<String, Object>, String>
{

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null) {
            return null; // Handle null value
        }
        try {
            return objectMapper.writeValueAsString(attribute); // Convert Map to JSON
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Map to JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null; // Handle null value
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to Map", e);
        }
    }

}
