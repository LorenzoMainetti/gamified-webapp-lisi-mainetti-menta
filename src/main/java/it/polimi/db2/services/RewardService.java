package it.polimi.db2.services;

import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Reward;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.security.InvalidParameterException;
import java.util.List;

@Stateless
public class RewardService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public RewardService(){
    }

    /**
     * Method to retrieve all the questionnaires related to a specific product
     * @param product the product you want to retrieve
     * @return the list of entries of the leaderboard
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product
     */
    public List<Reward> getLeaderboard(Product product) throws InvalidParameterException{
        List<Reward> leaderboard = em.createNamedQuery("Reward.getLeaderboard", Reward.class).setParameter(1, product).getResultList();
        if (leaderboard == null) {
            throw new InvalidParameterException("No questionnaires available for this product");
        }
        else {
            return leaderboard;
        }
    }
}
