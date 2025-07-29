package com.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class JsonReader {
    private static final String testDataFilePath = "src/test/resources/testData/";
    public static Map<String, String> readJsonFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file=new File(testDataFilePath + fileName);
            return mapper.readValue(file,new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to read test data", e);
        }

    }
}
