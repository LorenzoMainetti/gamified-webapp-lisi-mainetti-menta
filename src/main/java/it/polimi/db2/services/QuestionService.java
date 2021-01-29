package it.polimi.db2.services;

import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.LinkedList;
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
        Question base = questions.get(questions.size()-1);
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
    private Question copyProductInfoOptional(Question base, String text) {
        Question copy = new Question();
        copy.setProduct(base.getProduct());
        copy.setProductId(base.getProductId());
        copy.setText(text);
        copy.setMandatory(false);

        return copy;
    }

    /**
     * Method that returns the list of questions created for a product (both mandatory and optional)
     * @param mandatory list of mandatory questions
     * @param product product related to the questions
     * @return list of questions of the profuct
     */
    public List<Question> getAllQuestions(List<String> mandatory, Product product) {
        List<Question> allQuestions = new LinkedList<>();
        for (String question : mandatory) {
            Question questionObj = new Question();
            questionObj.setProductId(product.getProductId());
            questionObj.setMandatory(true);
            int questionNumber = mandatory.indexOf(question) +1;
            questionObj.setQuestionNumber(questionNumber);
            questionObj.setText(question);
            questionObj.setProduct(product);
            allQuestions.add(questionObj);
        }
        allQuestions = this.addStatQuestions(allQuestions);

        return (allQuestions);
    }

    /**
     * Method that sets the list of question to a product and then updates it in the DB
     * @param product product to which add the questions
     * @param questions list of questions to add to the product
     */
    public void updateProductQuestions(Product product, List<Question> questions) {
        product.setQuestions(questions);
        em.merge(product);
        em.flush();
    }
}
