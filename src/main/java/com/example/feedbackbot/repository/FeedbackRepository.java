package com.example.feedbackbot.repository;

import com.example.feedbackbot.model.Feedback;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Feedback feedback) {
        em.getTransaction().begin();
        em.persist(feedback);
        em.getTransaction().commit();
    }

    public List<Feedback> findAll() {
        return em.createQuery("SELECT f FROM Feedback f", Feedback.class).getResultList();
    }
}
