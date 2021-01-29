package it.polimi.db2.services;

import it.polimi.db2.entities.Answer;
import it.polimi.db2.entities.DirtyWords;
import it.polimi.db2.entities.Question;
import it.polimi.db2.entities.User;
import it.polimi.db2.entities.ids.AnswerKey;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class AnswerService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public AnswerService(){
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
