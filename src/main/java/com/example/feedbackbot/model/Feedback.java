package com.example.feedbackbot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String branch;
    private String message;
    private String sentiment;  // negative / neutral / positive
    private int severity;      // 1 - 5
    private LocalDateTime createdAt;
}
