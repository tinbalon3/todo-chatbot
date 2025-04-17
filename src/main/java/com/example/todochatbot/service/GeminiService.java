package com.example.todochatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String generateTodoSuggestion(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> content = new HashMap<>();
            content.put("role", "user");
            content.put("parts", List.of(
                Map.of("text", "Tạo một công việc todo dựa trên yêu cầu sau: " + prompt +
                              ". Chỉ trả về tiêu đề của công việc, không cần giải thích thêm.")
            ));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(content));

            String url = apiUrl + "?key=" + apiKey;
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            String response = restTemplate.postForObject(url, request, String.class);
            JsonNode jsonResponse = objectMapper.readTree(response);

            if (jsonResponse.has("candidates") && !jsonResponse.get("candidates").isEmpty()) {
                JsonNode content0 = jsonResponse.get("candidates").get(0).get("content");
                if (content0.has("parts") && !content0.get("parts").isEmpty()) {
                    return content0.get("parts").get(0).get("text").asText();
                }
            }
            
            return "Không thể tạo gợi ý cho công việc.";
        } catch (Exception e) {
            logger.error("Lỗi khi gọi Gemini API: ", e);
            return "Lỗi khi tạo gợi ý: " + e.getMessage();
        }
    }
}