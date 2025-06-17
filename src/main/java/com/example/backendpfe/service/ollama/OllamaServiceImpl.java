package com.example.backendpfe.service.ollama;

import com.example.backendpfe.models.ollama.CV;
import com.example.backendpfe.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OllamaServiceImpl implements OllamaService {

    private final RestTemplateBuilder restTemplateBuilder;
    private final ObjectMapper objectMapper;
    private final CVRepository cvRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final PersonalSkillRepository personalSkillRepository;
    private final SkillRepository skillRepository;
    private final NoteRepository noteRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public String getCompletion(String prompt) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = "http://193.95.30.59:11434/api/generate";

        Map<String, Object> request = new HashMap<>();
        request.put("model", "ons-chaima");
        request.put("prompt", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            System.out.println("Envoi de la requête POST à Ollama avec prompt: " + prompt);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            String[] lines = response.getBody().split("\n");
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                Map map = objectMapper.readValue(line, Map.class);
                builder.append(map.get("response"));
            }
            String result = builder.toString();
            System.out.println("Réponse reçue de Ollama: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à Ollama: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace
            throw new IllegalStateException("Error calling Ollama: " + e.getMessage(), e);
        }
    }

    @Override
    public CV extractCVFromJson(String jsonResponse) {
        try {
            System.out.println("JSON response to parse: " + jsonResponse);
            CV cv = objectMapper.readValue(jsonResponse, CV.class);
            System.out.println("CV parsed successfully: " + cv);
            return cv;

        } catch (Exception e) {
            System.err.println("Error while parsing CV JSON: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace
            throw new IllegalStateException("Failed to parse CV data from JSON", e);
        }
    }

    @Override
    public void saveParsedResponse(String jsonResponse) {
        try {

            CV toSave = cvRepository.save(new CV());
            CV extracted = extractCVFromJson(jsonResponse);
            extracted.getEducation().forEach(ed -> {
                ed.setCv(toSave);
                educationRepository.save(ed);
            });
            toSave.setEducation(extracted.getEducation());

            extracted.getExperience().forEach(exp -> {
                exp.setCv(toSave);
                experienceRepository.save(exp);
            });

            toSave.setExperience(extracted.getExperience());
            extracted.getProject().forEach(proj -> {
                proj.setCv(toSave);
                projectRepository.save(proj);
            });
            toSave.setProject(extracted.getProject());

            extracted.getPersonalSkills().forEach(psk -> {
                psk.setCv(toSave);
                personalSkillRepository.save(psk);
            });
            toSave.setPersonalSkills(extracted.getPersonalSkills());

            extracted.getTechnicalSkills().forEach(tsk -> {
                tsk.setCv(toSave);
                skillRepository.save(tsk);
            });
            toSave.setTechnicalSkills(extracted.getTechnicalSkills());

            extracted.getNotes().forEach(note -> {
                note.setCv(toSave);
                noteRepository.save(note);
            });
            toSave.setNotes(extracted.getNotes());


            cvRepository.save(toSave);
            System.out.println("CV saved successfully in the database with ID: " + toSave.getId());
        } catch (Exception e) {
            System.err.println("Error while saving CV: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace
            throw new IllegalStateException("Error saving CV: " + e.getMessage(), e);
        }
    }

}
