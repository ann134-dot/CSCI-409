package com.seniorproject.first.prototype.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//@Converter
public class ExperimentWordsConverter implements AttributeConverter<Map<Integer, String>, String> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Map<Integer, String> experimentWords) {
        String experimentWordsJSON = null;
        try{
            experimentWordsJSON = objectMapper.writeValueAsString(experimentWords);
        } catch (final JsonProcessingException e){
            System.out.println(e);
        }
        return experimentWordsJSON;
    }

    @Override
    public Map<Integer, String> convertToEntityAttribute(String experimentWordsJSON) {
        Map<Integer, String> experimentWords = null;
        try {
            experimentWords = objectMapper.readValue(experimentWordsJSON, new TypeReference<HashMap<Integer, String>>() {
            });
        }catch (final IOException e){
            System.out.println("JSON reading error!");
        }
        return experimentWords;
    }
}
