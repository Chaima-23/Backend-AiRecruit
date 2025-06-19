package com.example.backendpfe.controllers;
import com.example.backendpfe.models.ollama.test.Test;
import com.example.backendpfe.service.ollama.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ollama")
@RequiredArgsConstructor
@CrossOrigin(value = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class OllamaController {

    private final OllamaService ollamaService;

    // endpoint pour recevoir un prompt depuis Flask
    @PostMapping("/text")
    public ResponseEntity<String> processExtractedText(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        if (text == null || text.isEmpty()) {
            return ResponseEntity.badRequest().body("Texte manquant");
        }

        // Appeler Ollama dynamiquement avec le texte reçu
        String response = ollamaService.getCompletionForCv(text);

        // Enregistrement dans la BDD
        ollamaService.saveParsedResponse(response);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String prompt) {
        String response = ollamaService.getCompletionForCv(prompt);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test/save")
    public ResponseEntity<String> saveTest(@RequestBody String json) {
        ollamaService.saveParsedTest(json);
        return ResponseEntity.ok("Test sauvegardé avec succès !");
    }


    @GetMapping("/latest-test")
    public ResponseEntity<Map<String, Object>> getLatestTest() {
        try {
            Test test = ollamaService.getLatestTest();
            Map<String, Object> response = new HashMap<>();
            response.put("message", test != null ? "Latest test retrieved" : "No test available");
            response.put("data", test);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /*@GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from OllamaController");
    }*/
}