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
     * @throws PersistenceException
     * @throws InvalidParameterException
     */
    public List<Reward> getLeaderboard(Product product) throws PersistenceException, InvalidParameterException{
        List<Reward> leaderboard = null;
        leaderboard = em.createNamedQuery("Reward.getLeaderboard", Reward.class).setParameter(1, product).getResultList();
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
     * @throws PersistenceException
     * @throws InvalidParameterException
     */
    public Reward getReward(RewardKey rewardKey) throws PersistenceException, InvalidParameterException{
        List<Reward> rewardList = null;
        rewardList = em.createNamedQuery("Reward.getReward", Reward.class).setParameter(1, rewardKey).getResultList();
        if (rewardList == null) {
            throw new InvalidParameterException("invalid rewardKey");
        }
        else if(rewardList.size()==1) {
            return rewardList.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to insert a new entry in the reward table
     * @param points points related to the gamification of the questionnaire
     * @param user user who filled the questionnaire
     * @param product the product object of the questionnaire
     * @throws PersistenceException
     * @throws EJBTransactionRolledbackException
     */
    public void insertReward(int points, User user, Product product) throws PersistenceException, EJBTransactionRolledbackException {
        Reward reward = new Reward();
        RewardKey rewardKey = new RewardKey();
        rewardKey.setProductId(product.getProductId());
        rewardKey.setUserId(user.getUsername());
        reward.setId(rewardKey);
        reward.setUser(user);
        reward.setProduct(product);
        try {
            em.persist(reward);
            em.flush();
        } catch (EJBTransactionRolledbackException | PersistenceException e) {
            throw e;
        }
    }

    /**
     * Method to remove a questionnaire from the reward table
     * Removes all the entries related to a product
     * Could be used only by the ADMIN
     * @param product product to be removed
     * @return the number of elements removed
     */
    public int deleteQuestionnaire(Product product){
        em.flush(); //to store cached entities that could be possibly deleted by the following query
        Query query = em.createNamedQuery("Reward.deleteQuestionnaire", Reward.class);
        try{
            return query.setParameter(1, product).executeUpdate();
        }
        catch (EJBTransactionRolledbackException | PersistenceException e){
            throw e;
        }
    }

    /**
     * Method to compute the score of a questionnaire
     * if a mandatory answer is empty returns -1 (Exception is handled at servlet level)
     * @param answers list of all the answer of the user's questionnaire
     * @return score of the answers or -1 if there is an error
     */
    public int computeGamification(List<Answer> answers){
        int points = 0;
        for(Answer ans : answers){
            if(ans.getQuestion().isStatQuestion()){
                points += 2;
            }
            else{
                if(ans.getText().isEmpty()){
                    return -1;
                }
                else{
                    points += 1;
                }
            }
        }
        return points;
    }


}
