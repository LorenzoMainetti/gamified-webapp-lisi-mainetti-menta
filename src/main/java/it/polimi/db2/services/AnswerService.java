package it.polimi.db2.services;

import it.polimi.db2.entities.Answer;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.entities.User;
import it.polimi.db2.entities.ids.AnswerKey;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;
import java.util.List;

@Stateless
public class AnswerService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public AnswerService(){
    }

    /**
     * TODO
     * Methods that checks if an answer contains a bad word
     * @param answer answer to check
     * @return true if is acceptable otherwise false
     */
    public Boolean checkValidity(Answer answer){
        return true;
    }

    /**
     * Method that retrieves a specific answer given by a user to a question about a product
     * @param user user who answered
     * @param question question answered
     * @param product object of the questionnaire
     * @return the answer of the user
     */
    public Answer getSpecificAnswer(User user, Question question, Product product){
        List<Answer> ans = em.createNamedQuery("Answer.getSpecificAnswer", Answer.class).setParameter(1, user.getUsername())
                .setParameter(2, question.getQuestionId()).setParameter(3, product.getProductId()).getResultList();
        if (ans == null) {
            throw new InvalidParameterException("No answer present for this combination");
        }
        else if(ans.size()==1) {
            return ans.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    public void createAnswer(User user, int productId, Question question, String text) throws PersistenceException, EJBTransactionRolledbackException {
        AnswerKey answerKey = new AnswerKey();
        answerKey.setUserId(user.getUsername());
        answerKey.setQuestionId(question.getQuestionId());
        answerKey.setProductId(productId);

        Answer answer = new Answer();
        answer.setId(answerKey);
        answer.setText(text);
        answer.setUser(user);
        answer.setQuestion(question);

        try {
            em.persist(answer);
        } catch (EJBTransactionRolledbackException | PersistenceException e) {
            throw e;
        }
    }

}
