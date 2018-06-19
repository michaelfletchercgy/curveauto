package curveauto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    public static <T> T fromJson(String str, Class<T> clazz) throws IOException {
        return new ObjectMapper().readValue(str, clazz);
    }

    public static JsonNode toJsonNode(String str) throws IOException {
        return new ObjectMapper().readTree(str);
    }

    public static String toJson(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);

    }
}
