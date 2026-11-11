package com.orangehrm.framework.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/** Reads test data from JSON, keeping credentials and employee names outside test code. */
public final class JsonDataReader {
    private static final JsonNode ROOT = loadJson();

    private JsonDataReader() { }

    private static JsonNode loadJson() {
        try (InputStream input = JsonDataReader.class.getClassLoader().getResourceAsStream("testdata/testdata.json")) {
            if (input == null) {
                throw new IllegalStateException("testdata/testdata.json was not found on the test classpath.");
            }
            return new ObjectMapper().readTree(input);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static String get(String... path) {
        JsonNode node = ROOT;
        for (String key : path) {
            node = node == null ? null : node.get(key);
        }
        if (node == null || node.isNull()) {
            throw new IllegalArgumentException("Missing JSON test data at path: " + String.join(".", path));
        }
        return node.asText();
    }
}
