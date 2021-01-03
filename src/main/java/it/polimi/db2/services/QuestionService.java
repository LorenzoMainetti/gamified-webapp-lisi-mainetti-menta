package it.polimi.db2.services;

import it.polimi.db2.entities.Question;
import it.polimi.db2.entities.User;
import it.polimi.db2.entities.ids.QuestionKey;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.security.InvalidParameterException;
import java.util.List;

@Stateless
public class QuestionService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public QuestionService(){

    }

    /**
     * Method to add the fixed Statistical question to the Marketing ones
     * @param questions list of the Marketing questions created by the answer
     * @return the updated list
     */
    public List<Question> addStatQuestions(List<Question> questions) {
        Question base = questions.get(0);
        questions.add(copyProductInfoOptional(base, "What's your age?"));
        questions.add(copyProductInfoOptional(base,"What's your gender?"));
        questions.add(copyProductInfoOptional(base, "What's your experience level?"));

        return questions;
    }

    /**
     * Method that creates a copy of the base question with empty users and new text
     * @param base question to mimic
     * @param text text to insert in the new question
     * @return the copy
     */
    private Question copyProductInfoOptional(Question base, String text){
        Question copy = new Question();
        copy.setProduct(base.getProduct());
        copy.setProductId(base.getProductId());
        copy.setText(text);
        copy.setMandatory(false);
        return copy;

    }

    /**
     * Method to retrieve a question from its key
     * @param key identifier of the question
     * @return the searched question
     * @throws InvalidParameterException
     */
    public Question getQuestion(QuestionKey key)throws InvalidParameterException {
        List<Question> result = em.createNamedQuery("Question.getQuestion", Question.class).setParameter(1, key.getQuestionId()).setParameter(2, key.getProductId())
                .getResultList();
        if (result == null) {
            throw new InvalidParameterException("productID or questionID are wrong");
        }
        else if(result.size()==1) {
            return result.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * UNDERSTAND IF IT IS NECESSARY!!!
     * Method that updates the list of user who answered to a question
     * @param key identifier of the question
     * @param user user who filled the question
     */
    public void updateUserRef(QuestionKey key, User user){
        Question question = getQuestion(key);
        question.getUsers().add(user);
        em.merge(question);
        em.flush();
    }
}
