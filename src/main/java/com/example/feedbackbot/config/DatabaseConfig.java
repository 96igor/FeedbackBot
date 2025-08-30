package com.example.feedbackbot.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConfig {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("feedbackPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
