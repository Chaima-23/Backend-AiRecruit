package com.example.backendpfe.service.ollama;

import com.example.backendpfe.models.ollama.CV;
import java.util.List;

public interface OllamaService {
    String getCompletionForCv(String prompt);
    void saveParsedResponse(String jsonResponse);
    CV extractCVFromJson(String jsonResponse);


    String getCompletionForTest(List<String> skills, int numberOfQuestions);
    void saveTestFromJson(String jsonResponse);

}
