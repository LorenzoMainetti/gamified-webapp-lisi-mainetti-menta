package it.polimi.db2.services;

import com.google.gson.Gson;
import it.polimi.db2.admin.AdminHomePageContent;
import it.polimi.db2.entities.*;
import it.polimi.db2.exception.ProductNotFoundException;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Tuple;

import java.security.InvalidParameterException;
import java.util.*;

@Stateless
public class ProductService {

    @EJB(name = "it.polimi.db2.entities.services/QuestionService")
    private QuestionService questionService;

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


     * @throws PersistenceException if a problem happens managing the entity (for example it already exists)
     * @throws IllegalArgumentException if the argument of the persist is not an entity
     */
    public Product insertProduct(String name, Date date, String description, Admin admin) throws PersistenceException, IllegalArgumentException{


        Product product = new Product();
        product.setName(name);
        product.setDate(date);

        product.setDescription(description);
        product.setCreatorId(admin.getAdminId());

        admin.getCreatedProducts().add(product);

 //TODO this should be better due to cascading, product should have the admin automatically

        em.persist(product);


        return product;

    }



    /**
     * Method to retrieve the questionnaire related to a specific product
     * @param productId id of the requested product
     * @return the product searched
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product
     */
    public Product getProduct(int productId) throws InvalidParameterException{
        List<Product> products = em.createNamedQuery("Product.getProduct", Product.class).setParameter(1, productId)
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
     * @throws InvalidParameterException if the product does not exist or there is more than 1 product, or the user hasn't filled the questionnaire
     * @// TODO: 05/01/2021 check if it's still usable
     */
    public List<Answer> getUserAnswers(Product product, String user) throws InvalidParameterException{
        List<User> users = getProductUsers(product, false);
        ArrayList<String> ids = new ArrayList<>();
        for(User u :users){
            ids.add(u.getUsername());
        }
        if(ids.contains(user)){
            throw new InvalidParameterException("The user has cancelled his questionnaire, so there are no answers");
        }
        else {
            List<Answer> ans = em.createNamedQuery("Answer.getUserAnswers", Answer.class).setParameter(1, user).setParameter(2, product.getProductId()).getResultList();
            if (ans == null || ans.isEmpty()) {
                throw new InvalidParameterException("No user's answers about this product");
            } else {
                return ans;
            }
        }
    }


    private Set<String> getTable (List <User> users) {
        Set<String> success = new HashSet<>();
        for (User user : users) {
            success.add(user.getUsername());
        }
        return success;

    }

    private boolean exists(User u, Set<String> usernames) {
        return usernames.contains(u.getUsername());
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
        List<User> usersSucessfullyCompleted = em.createNamedQuery("Answer.getUserFill", User.class).setParameter(1, product.getProductId()).getResultList();

        if (usersSucessfullyCompleted == null || usersSucessfullyCompleted.isEmpty()) {
            if(QuestFilled){
                return null;
            }
            else{
                return product.getUsers();
            }
        }
        else {
            if(QuestFilled){
                return usersSucessfullyCompleted;
            }
            //there is a list of user but I need to fetch only the one that had canceled the questionnaire
            else{
                //to be filled
                List<User> notFilled = new LinkedList<>();

                //list of username of accounts that have a leaderboard entry
                List <User> relatedToProduct = product.getUsers();
                Set<String> successfullyCompletedHash = getTable(usersSucessfullyCompleted);

                //for each leaderboard entry
                for(User u : relatedToProduct){
                    //check if this user has successfully completed the questionnaire
                    if (!exists(u, successfullyCompletedHash)) notFilled.add(u);
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


    public void dummyImageLoad(int productId, byte[] img) throws IllegalArgumentException, PersistenceException{
        Product product = em.find(Product.class, productId);
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
