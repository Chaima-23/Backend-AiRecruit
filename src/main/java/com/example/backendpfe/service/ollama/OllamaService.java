package com.example.backendpfe.service.ollama;

import com.example.backendpfe.models.ollama.CV;
import java.util.List;

public interface OllamaService {
    String getCompletion(String prompt);
    void saveParsedResponse(String jsonResponse);
    CV extractCVFromJson(String jsonResponse);
}
