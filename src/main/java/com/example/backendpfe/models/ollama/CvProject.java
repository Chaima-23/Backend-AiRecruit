    package com.example.backendpfe.models.ollama;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class CvProject {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;
        @ManyToOne
        private CV cv;
        private String name;
        private String startDate;
        private String endDate;

        @Column(length = 4000)
        private String description;
    }

