package it.polimi.db2.services;

import it.polimi.db2.entities.Review;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Stateless
public class ReviewService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public ReviewService(){
    }

    /**
     * Method to get a fixed number of random reviews
     * @return list of reviews
     * @throws InvalidParameterException if there are no review with the argument number
     */
    public ArrayList<String> getRandomReviews() throws InvalidParameterException {
        ArrayList<String> result = new ArrayList<>();
        long totalReview = em.createNamedQuery("Review.getReviewNumber", long.class).getSingleResult();
        int reviewNumber = 5;
        if((int) totalReview < reviewNumber){
            throw new InvalidParameterException("Not enough review in the DB");
        }
        else {
            Random random = new Random();
            List<Review> review;
            ArrayList<Integer> selected = new ArrayList<>();
            int index;
            int i = 0;
            while (i < reviewNumber) {
                index = (random.nextInt((int) totalReview - 1) + 1);
                if (selected.contains(index))
                    continue;
                review = em.createNamedQuery("Review.getReviewById", Review.class).setParameter(1, index).getResultList();
                selected.add(index);
                result.add(review.get(0).getText());
                i++;
            }
            return result;
        }
    }
}
