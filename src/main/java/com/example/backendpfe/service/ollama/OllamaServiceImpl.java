package com.example.backendpfe.service.ollama;

import com.example.backendpfe.models.ollama.CV;
import com.example.backendpfe.models.ollama.CvEducation;
import com.example.backendpfe.models.ollama.CvExperience;
import com.example.backendpfe.models.ollama.CvProject;
import com.example.backendpfe.models.ollama.test.Question;
import com.example.backendpfe.models.ollama.test.Test;
import com.example.backendpfe.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OllamaServiceImpl implements OllamaService {

    private final RestTemplateBuilder restTemplateBuilder;
    private final ObjectMapper objectMapper;

    private final CVRepository cvRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final ProjectRepository projectRepository;

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional(readOnly = true)
    public String getCompletionForCv(String prompt) {
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
            e.printStackTrace();
            throw new IllegalStateException("Error calling Ollama: " + e.getMessage(), e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public String getCompletionForTest(List<String> skills, int numberOfQuestions) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = "http://193.95.30.59:11434/api/generate";

        Map<String, Object> request = new HashMap<>();
        request.put("model", "ons-chaima-test");
        request.put("prompt", "%s %s".formatted(numberOfQuestions, String.join(" ", skills)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
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
            e.printStackTrace();
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
            e.printStackTrace();
            throw new IllegalStateException("Failed to parse CV data from JSON", e);
        }
    }

    @Override
    @Transactional
    public void saveParsedResponse(String jsonResponse) {
        try {
            CV raw = extractCVFromJson(jsonResponse);

            CV tempCv = new CV();
            // Optionnel : enregistrer les infos de base si le modèle CV contient ces champs


            final CV cv = cvRepository.save(tempCv); // persist pour obtenir l'id
            if (raw.getEmail() != null) {
                cv.setEmail(new ArrayList<>(raw.getEmail()));
            }

            cv.setPhone(new ArrayList<>(raw.getPhone()));
            cv.setName(raw.getName());
            cv.setSpecialization(raw.getSpecialization());

            if (raw.getTechnicalSkills() != null) {
                cv.setTechnicalSkills(raw.getTechnicalSkills());
            }

            if (raw.getPersonalSkills() != null) {
                cv.setPersonalSkills(raw.getPersonalSkills());
            }
            if (raw.getEducation() != null) {
                // Formations
                List<CvEducation> educations = raw.getEducation().stream().map(edu ->
                        CvEducation.builder()
                                .cv(cv)
                                .startDate(edu.getStartDate())
                                .endDate(edu.getEndDate())
                                .description(edu.getDescription())
                                .build()
                ).toList();
                educations = new ArrayList<>(educationRepository.saveAll(new ArrayList<>(educations)));
                cv.setEducation(educations);

            }

            if (raw.getExperience() != null) {
                // Expériences
                List<CvExperience> experiences = raw.getExperience().stream().map(exp ->
                        CvExperience.builder()
                                .cv(cv)
                                .startDate(exp.getStartDate())
                                .endDate(exp.getEndDate())
                                .description(exp.getDescription())
                                .build()
                ).toList();
                experiences = new ArrayList<>(experienceRepository.saveAll(new ArrayList<>(experiences)));
                cv.setExperience(experiences);

            }

            if (raw.getProject() != null) {
                // Projets
                List<CvProject> projects = raw.getProject().stream().map(proj ->
                        CvProject.builder()
                                .cv(cv)
                                .name(proj.getName())
                                .startDate(proj.getStartDate())
                                .endDate(proj.getEndDate())
                                .description(proj.getDescription())
                                .build()
                ).toList();
                projects = new ArrayList<>(projectRepository.saveAll(new ArrayList<>(projects)));
                cv.setProject(projects);

            }


            cv.setNotes(raw.getNotes());

            // Sauvegarde finale
            cvRepository.save(cv);

            System.out.println("CV saved successfully with mapped data.");
            String completionForTest = getCompletionForTest(cv.getTechnicalSkills(), 10);


            System.out.println("Test generated successfully: " + completionForTest);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du CV : " + e.getMessage());
            e.printStackTrace();
            throw new IllegalStateException("Error saving CV", e);
        }
    }

    @Override
    @Transactional
    public void saveTestFromJson(String jsonResponse) {
        try {

            Test raw = objectMapper.readValue(jsonResponse, Test.class);
            System.out.println("Test parsed successfully: " + raw.getTitle());


            Test tempTest = new Test();
            Test test = testRepository.save(tempTest);


            test.setTitle(raw.getTitle());

            if (raw.getQuestions() != null) {
                List<Question> questions = raw.getQuestions().stream().map(q ->
                        Question.builder()
                                .test(test) // association
                                .content(q.getContent())
                                .type(q.getType())
                                .options(q.getOptions())
                                .correctOptions(q.getCorrectOptions())
                                .build()
                ).toList();

                questions = new ArrayList<>(questionRepository.saveAll(questions));
                test.setQuestions(questions);
            }
            testRepository.save(test);

            System.out.println("Test enregistré avec succès dans la base de données.");

        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du test : " + e.getMessage());
            e.printStackTrace();
            throw new IllegalStateException("Erreur lors de l'enregistrement du test", e);
        }
    }

}



