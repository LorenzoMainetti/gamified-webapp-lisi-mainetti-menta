package it.polimi.db2.services;

import it.polimi.db2.auxiliary.UserStatus;
import it.polimi.db2.entities.Answer;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.User;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Stateless

public class UserService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public UserService() {
    }

    /**
     * Method to insert a new User in the DB
     * @param username username of the User
     * @param email email of the User
     * @param password password of the User
     * @param banned ban flag of the User
     * @throws PersistenceException if a problem happens managing the entity (for example it already exists)
     * @throws IllegalArgumentException if the argument of the persist is not an entity
     */
    public void insertUser(String username, String email, String password, boolean banned) throws PersistenceException, IllegalArgumentException{
        User user = new User();
        user.setBanned(banned);
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        em.persist(user);
    }

    /**
     * Method to get a specific user
     * @param username username of the User to retrieve
     * @return the user requested
     * @throws InvalidParameterException if the user does not exist
     */
    public User getUser(String username) throws InvalidParameterException {
        List<User> usersFromDB = em.createNamedQuery("User.getUser", User.class).setParameter(1, username).getResultList();
        if (usersFromDB == null || usersFromDB.isEmpty()) {
            throw new InvalidParameterException("No User with this username");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to check the User credentials
     * @param username username of the User
     * @param password password of the User
     * @return User with corresponding fields
     * @throws InvalidParameterException if the user does not exist
     */
    public User checkCredentials(String username, String password) throws InvalidParameterException {
        List<User> usersFromDB = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2, password)
                .getResultList();
        if (usersFromDB == null || usersFromDB.isEmpty()) {
            throw new InvalidParameterException("Provided username or password is wrong");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to ban the user
     * @param username username of the User
     * @throws PersistenceException if updateProfile fails
     * @throws IllegalArgumentException if the user does not exist
     */
    public void banUser(String username) throws PersistenceException, IllegalArgumentException{
        User toBeBanned = getUser(username);
        toBeBanned.setBanned(true);
        updateProfile(toBeBanned);
    }

    /**
     * Method to update a User
     * @param user user to be updated
     * @throws PersistenceException if a problem happens managing the entity (for example it does not exists)
     * @throws IllegalArgumentException if the argument of the merge is not an entity or is a removed entity
     */
    public void updateProfile(User user) throws PersistenceException, IllegalArgumentException {
        em.merge(user);
    }

    public UserStatus checkUserStatus(User user, Product product, ProductService productService) throws InvalidParameterException{
        if(user.isBanned()){
            return UserStatus.BANNED;
        }
        else{
            List<Answer> ans = em.createNamedQuery("Answer.getUserAnswers", Answer.class)
                    .setParameter(1, user.getUsername()).setParameter(2, product.getProductId()).getResultList();
            if (ans == null || ans.isEmpty()) {
                //no user answer related to the product
                List<User> cancelled = productService.getProductUsers(product, false);
                ArrayList<String> ids = new ArrayList<>();
                for(User u :cancelled){
                    ids.add(u.getUsername());
                }
                if(ids.contains(user.getUsername())){
                    return UserStatus.DELETED;
                }
                else {
                    return UserStatus.NOT_COMPLETED;
                }
            }
            else {
                return UserStatus.COMPLETED;
            }
        }
    }
}
