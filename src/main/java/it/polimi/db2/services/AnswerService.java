package it.polimi.db2.services;

import it.polimi.db2.entities.*;
import it.polimi.db2.entities.ids.AnswerKey;
import it.polimi.db2.entities.ids.QuestionKey;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class AnswerService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public AnswerService(){
    }

    /**
     * Method that checks if an answer contains a bad word
     * @param answerText answer to be checked
     * @return true if is acceptable otherwise false
     */
    public Boolean checkValidity(String answerText) {
        String[] splitted = answerText.split(" ");
        List<DirtyWords> dirtyWords = new ArrayList<>();
        for (String s : splitted) {
            dirtyWords.addAll(em.createNamedQuery("DirtyWords.checkAnswer", DirtyWords.class).setParameter(1, s).getResultList());
            if(!dirtyWords.isEmpty())
                break;
        }
        return dirtyWords.isEmpty();
    }

    /**
     * Method that checks if an answer contains a bad word
     * @param string answer to be checked
     * @return true if it contains at least one bad word otherwise false
     * @throws InvalidParameterException if the sentence is not properly formatted
     */
    public boolean multipleWordsCheck(String string) throws InvalidParameterException{
        String[] words = string.split("\\W+");
        ArrayList<String> sentence = new ArrayList<>(Arrays.asList(words));
        List<DirtyWords> result = em.createNamedQuery("DirtyWords.CheckSentence", DirtyWords.class).setParameter(1, sentence).getResultList();
        return !result.isEmpty();
    }

    /**
     * Method that retrieves a specific answer given by a user to a question about a product
     * @param user user who answered
     * @param question question answered
     * @param product object of the questionnaire
     * @return the answer of the user
     * @throws InvalidParameterException if the product, the question or the answer do not exists
     */
    public Answer getSpecificAnswer(User user, Question question, Product product) throws InvalidParameterException{
        List<Answer> ans = em.createNamedQuery("Answer.getSpecificAnswer", Answer.class).setParameter(1, user.getUsername())
                .setParameter(2, question.getQuestionId()).setParameter(3, product.getProductId()).getResultList();
        if (ans == null || ans.isEmpty()) {
            throw new InvalidParameterException("No answer present for this combination");
        }
        else if(ans.size()==1) {
            return ans.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method that creates in the db an answer given its parameters
     * @param user user who filled the answer
     * @param question question who belongs the answer
     * @param text text of the answer
     * @throws PersistenceException if a problem happens managing the entity (for example it already exists)
     * @throws IllegalArgumentException if the argument of the persist is not an entity
     */
    public void insertAnswer(User user, Question question, String text) throws PersistenceException, IllegalArgumentException {

        AnswerKey answerKey = new AnswerKey();
        answerKey.setUserId(user.getUsername());
        answerKey.setQuestionKey(question.getQuestionKey());

        Answer answer = new Answer();
        answer.setId(answerKey);
        answer.setText(text);
        answer.setUser(user);
        answer.setQuestion(question);

        em.persist(answer);
    }
  



}
