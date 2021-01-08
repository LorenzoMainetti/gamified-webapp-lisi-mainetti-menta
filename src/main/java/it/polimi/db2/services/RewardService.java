package it.polimi.db2.services;

import it.polimi.db2.entities.*;
import it.polimi.db2.entities.ids.RewardKey;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

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

    /**
     * Method to retrieve a specific questionnaire
     * @param rewardKey key of the the reward table, containing both user and product information
     * @return the reward entity desired
     * @throws InvalidParameterException if the reward does not exist or there is more than 1 reward
     */
    public Reward getReward(RewardKey rewardKey) throws InvalidParameterException{
        List<Reward> rewardList = em.createNamedQuery("Reward.getReward", Reward.class).setParameter(1, rewardKey).getResultList();
        if (rewardList == null || rewardList.isEmpty()) {
            throw new InvalidParameterException("Invalid rewardKey");
        }
        else if(rewardList.size()==1) {
            return rewardList.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to remove a questionnaire from the reward table
     * Removes all the entries related to a product
     * Could be used only by the ADMIN
     * @param product product to be removed
     * @return the number of elements removed
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product
     * @// TODO: 06/01/2021 check if it's still needed
     */
    public int deleteQuestionnaire(Product product) throws InvalidParameterException{
        em.flush(); //to store cached entities that could be possibly deleted by the following query
        return em.createNamedQuery("Reward.deleteQuestionnaire", Reward.class).setParameter(1, product).executeUpdate();
    }
}
