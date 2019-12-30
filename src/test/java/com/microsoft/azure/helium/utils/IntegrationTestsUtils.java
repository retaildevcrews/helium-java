package com.microsoft.azure.helium.utils;

import java.io.IOException;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * IntegrationTestsUtils
 */
public class IntegrationTestsUtils {

    public static byte[] serializeObject(Object value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(value);
    }

    public static int getRandomBetween(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }
}