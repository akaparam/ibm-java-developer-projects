package com.retailstore.feedback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private static final String URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String generateContent(String prompt) {
        try {
            ObjectNode root = mapper.createObjectNode();
            ArrayNode contents = mapper.createArrayNode();
            ObjectNode content = mapper.createObjectNode();
            ArrayNode parts = mapper.createArrayNode();

            parts.add(mapper.createObjectNode().put("text", prompt));
            content.set("parts", parts);
            contents.add(content);
            root.set("contents", contents);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request =
                new HttpEntity<>(root.toString(), headers);

            ResponseEntity<String> response =
                restTemplate.exchange(URL + "?key=" + apiKey,
                        HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            return "{}";
        }
    }
}
