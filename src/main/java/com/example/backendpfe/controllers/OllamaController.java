package com.example.backendpfe.controllers;
import com.example.backendpfe.service.ollama.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ollama")
@RequiredArgsConstructor
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
        ollamaService.saveTestFromJson(json);
        return ResponseEntity.ok("Test sauvegardé avec succès !");
    }


    /*@GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from OllamaController");
    }*/
}