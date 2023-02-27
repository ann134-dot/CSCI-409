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
public class ExperimentOverallResultsConverter implements AttributeConverter<Map<Integer, Integer>, String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Map<Integer, Integer> experimentOverallResults) {
        String experimentOverallResultsJSON = null;
        try {
            experimentOverallResultsJSON = objectMapper.writeValueAsString(experimentOverallResults);
        }catch (final JsonProcessingException e){
            System.out.println(e);
        }
        return experimentOverallResultsJSON;
    }

    @Override
    public Map<Integer, Integer> convertToEntityAttribute(String experimentOverallResultsJSON) {
        Map<Integer, Integer> experimentOverallResults = null;
        try {
            experimentOverallResults = objectMapper.readValue(experimentOverallResultsJSON, new TypeReference<HashMap<Integer, Integer>>() {
            });
        }catch (final IOException e){
            System.out.println("JSON reading error!");
        }
        return experimentOverallResults;
    }
}
