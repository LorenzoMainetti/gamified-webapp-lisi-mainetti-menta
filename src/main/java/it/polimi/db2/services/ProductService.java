package it.polimi.db2.services;

import com.google.gson.Gson;
import it.polimi.db2.admin.AdminHomePageContent;
import it.polimi.db2.entities.*;
import it.polimi.db2.exception.ProductNotFoundException;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Tuple;

import java.io.Serializable;
import java.security.InvalidParameterException;
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
        admin.getCreatedProducts().add(product); //TODO this should be better due to cascading, product should have the admin automatically
        //product.setCreator(admin);
        try {
            em.persist(product);
        } catch (EJBTransactionRolledbackException | PersistenceException e) {
            throw e;
        }
        return product;
    }

    /**
     * Method to retrieve the questionnaire related to a specific product
     * @param productId id of the requested product
     * @return the product searched
     */
    public Product getProduct(int productId) throws InvalidParameterException{
        List<Product> products = em.createNamedQuery("Product.getProduct", Product.class).setParameter(1, productId)
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

    public Product getProductOfTheDay() throws InvalidParameterException {
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
        //TODO I changed the NamedQuery, now it retrieves the whole product entity not just some attributes
        Map <Date, String> queryResult = (Map<Date, String>) em.createNamedQuery("Product.getPastProducts", Tuple.class).setParameter(1, currentDate);
        return queryResult;
    }

    public List<Product> getPastQuestionnaires(Date currentDate) throws InvalidParameterException {
        List<Product> products = em.createNamedQuery("Product.getPastProducts", Product.class).setParameter(1,  currentDate)
                .getResultList();
        if (products.isEmpty()) {
            throw new InvalidParameterException("no product found");
        }
        else {
            return products;
        }
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

    public void deleteProduct(Product product) {
        for(User user : product.getUsers())
            user.removeProduct(product);
        em.remove(product);
    }

    /**
     * @author ale
     * @param productId pk for the product that will be retrieved from the database
     * @return a json serialized version of the product
     * @throws ProductNotFoundException if product do not exist or internal server error
     */
    public String getProductToGson(int productId) throws ProductNotFoundException {
        List<Product> products = em.createNamedQuery("Product.getProduct", Product.class).setParameter(1, productId)
                .getResultList();
        if (products == null) {
            throw new ProductNotFoundException("invalid productID");
        }
        else if(products.size()==1) {

            Product prod = products.get(0);
            Gson gson = new Gson();
            AdminHomePageContent content = new AdminHomePageContent();

            String encodedImage = Base64.getEncoder().encodeToString(prod.getImage());
            content.setProdDescription(prod.getDescription());
            content.setProdName(prod.getName());
            content.setEncodedImg(encodedImage);
            return gson.toJson(content);
        }
        else {
            throw new ProductNotFoundException("internal database error");
        }
    }

}
