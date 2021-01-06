package it.polimi.db2.services;

import it.polimi.db2.entities.*;
import it.polimi.db2.exception.ProductNotFoundException;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Tuple;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Stateless
public class ProductService {

    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public ProductService(){
    }

    /**
     * Method that creates and inserts a new questionnaire in the Database
     * @param name name of the product
     * @param date date of the questionnaire
     * @param description small description of the product
     * @param admin the admin who created the questionnaire
     * @throws EJBTransactionRolledbackException
     * @throws PersistenceException
     */
    public Product insertProduct(String name, Date date, String description, Admin admin) throws EJBTransactionRolledbackException, PersistenceException{
        Product product = new Product();
        product.setName(name);
        product.setDate(date);
        product.setDescription(description);
        product.setCreatorId(admin.getAdminId());
        product.setCreator(admin);
        try {
            em.persist(product);
        } catch (EJBTransactionRolledbackException | PersistenceException e) {
            throw e;
        }
        return product;
    }

    /**
     * Method to retrieve the questionnaire related to a specific product
     * @param productID identifier of the product
     * @return the product searched
     */
    public Product getProduct(int productID) throws InvalidParameterException{
        List<Product> products = em.createNamedQuery("Product.getProduct", Product.class).setParameter(1, productID)
                .getResultList();
        if (products == null) {
            throw new InvalidParameterException("invalid productID");
        }
        else if(products.size()==1) {
            return products.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    public Product getProductOfTheDay() throws InvalidParameterException{
        List<Product> products = em.createNamedQuery("Product.getProductDummy", Product.class).setParameter(1, "Barca Giocattolo")
                .getResultList();
        if (products == null) {
            throw new InvalidParameterException("product not found");
            //throw new ProductNotFoundException("no product of the day has been found"); //todo da sostituire
        }
        else if(products.size()==1) {
            return products.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    public Map<Date, String> getPastQuestionnairesMinimal(Date currentDate) {

        Map <Date, String> queryResult = (Map<Date, String>) em.createNamedQuery("Product.getPastProducts", Tuple.class).setParameter(1, currentDate);
        return queryResult;
    }
    /**
     * Method that retrieves the answers of a user about a product
     * @param product product of interest
     * @param user user who compiled the questionnaire
     * @return list of user's answers
     * @throws InvalidParameterException
     */
    public List<Answer> getUserAnswers(Product product, User user) throws InvalidParameterException{
        List<User> users = getProductUsers(product, false);
        if(users.contains(user)){
            throw new InvalidParameterException("The user has cancelled his questionnaire, so there are no answers");
        }
        else {
            List<Answer> ans = em.createNamedQuery("Answer.getUserAnswers", Answer.class).setParameter(1, user.getUsername()).setParameter(2, product.getProductId()).getResultList();
            if (ans == null) {
                throw new InvalidParameterException("No user's answers about this product");
            } else {
                return ans;
            }
        }
    }

    /**
     * Method that returns the list of user who compiled the questionnaire (if QuestFilled is true),
     * otherwise the list of user who cancelled their questionnaire
     * @param product object of the questionnaire
     * @param QuestFilled for filled questionnaire, false for empty ones
     * @return list of users
     */
    public List<User> getProductUsers(Product product, Boolean QuestFilled){
        List<User> users = em.createNamedQuery("Answer.getUserFill", User.class).setParameter(1, product.getProductId()).getResultList();
        if (users == null) {
            if(QuestFilled){
                return null;
            }
            else{
                return product.getUsers();
            }
        }
        else {
            if(QuestFilled){
                return users;
            }
            else{
                List<User> notFilled = new ArrayList<>();
                for(User u : product.getUsers()){
                    if(!users.contains(u)){
                        notFilled.add(u);
                    }
                }
                return notFilled;
            }
        }
    }

    public static List<String> getQuestions(Product product) {

        List <String> questionTexts = new ArrayList<>();
        List<Question> questions = product.getQuestions();
        questions.forEach( question -> {
            questionTexts.add(question.getText());
        });
        questionTexts.remove(questionTexts.size()-1);
        questionTexts.remove(questionTexts.size()-1);
        questionTexts.remove(questionTexts.size()-1);
        return questionTexts;

    }

    public void dummyImageLoad(Product product, byte[] img){
        product.setImage(img);
        em.merge(product);
        em.flush();
    }
}
