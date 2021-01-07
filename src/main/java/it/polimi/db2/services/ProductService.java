package it.polimi.db2.services;

import it.polimi.db2.entities.*;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

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
     * Methpd that creates and inserts a new questionnaire in the Database
     * @param name name of the product
     * @param date date of the questionnaire
     * @param description small description of the product
     * @param admin the admin who created the questionnaire
     * @param questions list of questions created by the admin
     * @throws PersistenceException if a problem happens managing the entity (for example it already exists)
     * @throws IllegalArgumentException if the argument of the persist is not an entity
     */
    public void insertProduct(String name, Date date, String description, Admin admin, List<Question> questions) throws PersistenceException, IllegalArgumentException{
        Product product = new Product();
        product.setName(name);
        product.setDate(date);
        product.setDescription(description);
        product.setQuestions(questions);
        product.setCreator(admin);

        em.persist(product);
    }

    /**
     * Method to retrieve the questionnaire related to a specific product
     * @param productID identifier of the product
     * @return the product searched
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product
     */
    public Product getProduct(int productID) throws InvalidParameterException{
        List<Product> products = em.createNamedQuery("Product.getProduct", Product.class).setParameter(1, productID)
                .getResultList();
        if (products == null || products.isEmpty()) {
            throw new InvalidParameterException("Invalid productID");
        }
        else if(products.size()==1) {
            return products.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to retrieve the questionnaire related to the product of the day
     * @return the product of the day
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product
     * @// TODO: 05/01/2021 remove dummy query
     */
    public Product getProductOfTheDay() throws InvalidParameterException {
        List<Product> products = em.createNamedQuery("Product.getProductDummy", Product.class).setParameter(1, "Barca Giocattolo").getResultList();
        //Date date = java.sql.Date.valueOf(LocalDate.now());
        //List<Product> products = em.createNamedQuery("Product.getProductOfTheDay", Product.class).setParameter(1, date).getResultList();
        if (products == null || products.isEmpty()) {
            throw new InvalidParameterException("No product of the Day");
        }
        else if(products.size()==1) {
            return products.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method that retrieves the answers of a user about a product
     * @param product product of interest
     * @param user user who compiled the questionnaire
     * @return list of user's answers
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product, or the user hasn't filled the questionnaire
     * @// TODO: 05/01/2021 check if it's still usable
     */
    public List<Answer> getUserAnswers(Product product, User user) throws InvalidParameterException{
        List<User> users = getProductUsers(product, false);
        ArrayList<String> ids = new ArrayList<>();
        for(User u :users){
            ids.add(u.getUsername());
        }
        if(ids.contains(user.getUsername())){
            throw new InvalidParameterException("The user has cancelled his questionnaire, so there are no answers");
        }
        else {
            List<Answer> ans = em.createNamedQuery("Answer.getUserAnswers", Answer.class).setParameter(1, user.getUsername()).setParameter(2, product.getProductId()).getResultList();
            if (ans == null || ans.isEmpty()) {
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
     * @throws InvalidParameterException if the product does not exist
     */
    public List<User> getProductUsers(Product product, Boolean QuestFilled) throws InvalidParameterException{
        List<User> users = em.createNamedQuery("Answer.getUserFill", User.class).setParameter(1, product.getProductId()).getResultList();
        if (users == null || users.isEmpty()) {
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

    /**
     * Method that returns the questions related to a specific product
     * @param product product to which belong the questions
     * @return list of questions in string format, excluding the statistical ones
     * @throws InvalidParameterException if there are no question related to the product
     */
    public static List<String> getQuestions(Product product) throws InvalidParameterException{
        List <String> questionTexts = new ArrayList<>();
        List<Question> questions = product.getQuestions();
        if(questions == null || questions.isEmpty()){
            throw new InvalidParameterException("No questions present related to the product");
        }
        else {
            questions.forEach(question -> {
                questionTexts.add(question.getText());
            });
            questionTexts.remove(questionTexts.size() - 1);
            questionTexts.remove(questionTexts.size() - 1);
            questionTexts.remove(questionTexts.size() - 1);
            return questionTexts;
        }
    }

    /**
     * SUDO Method to set an image to a product in the db
     * @param product product to which add the photo
     * @param img photo to add
     * @throws PersistenceException if a problem happens managing the entity (for example it does not exists)
     * @throws IllegalArgumentException if the argument of the merge is not an entity or it's a removed entity
     */
    public void dummyImageLoad(Product product, byte[] img) throws IllegalArgumentException, PersistenceException{
        product.setImage(img);
        em.merge(product);
        em.flush();
    }

    public void addCancelledUser(Product product, User user) throws IllegalArgumentException, PersistenceException{
        product.getUsers().add(user);
        user.getProducts().add(product);
        em.merge(product);
        em.merge(user);
        em.flush();
    }
}
