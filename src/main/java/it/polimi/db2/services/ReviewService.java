package it.polimi.db2.services;

import it.polimi.db2.entities.Review;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Stateless
public class ReviewService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;
    private final int reviewNumber = 5;

    public ReviewService(){
    }

    public ArrayList<String> getRandomReviews(){
        ArrayList<String> result = new ArrayList<>();
        long totalReview = em.createNamedQuery("Review.getReviewNumber", long.class).getSingleResult();
        Random random = new Random();
        List<Review> review;
        for(int i=0; i<reviewNumber; i++){
            review = em.createNamedQuery("Review.getReviewById", Review.class)
                    .setParameter(1,( random.nextInt((int)totalReview -1) + 1)).getResultList();
            result.add(review.get(0).getText());
        }
        return result;
        }
    }
