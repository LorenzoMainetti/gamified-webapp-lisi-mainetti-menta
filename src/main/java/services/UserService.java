package services;

import entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@Stateless

public class UserService {
    @PersistenceContext(unitName = "gamifiedapp")
    private EntityManager em;

    public UserService() {
    }

    //TODO da capire se è stato fatto bene
    public void insertUser(String username, String email, String password, boolean banned) throws PersistenceException{
        try {
            User user = new User();
            user.setBanned(true);
            user.setEmail(email);
            user.setPassword(password);
            user.setUsername(email);
            em.persist(user);
        }
        catch (PersistenceException e) {
            throw e;
        }
    }

    private User getUser(String username) throws PersistenceException, InvalidParameterException {
        List<User> usersFromDB = null;
        try {
            usersFromDB = em.createNamedQuery("User.getUser", User.class).setParameter(1, username)
                    .getResultList();
        } catch (PersistenceException e) {
            throw e;
        }
        if (usersFromDB == null) {
            throw new InvalidParameterException("Provided username or password is wrong");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("Database error");
        }
    }

    public User checkCredentials(String username, String password) throws PersistenceException, InvalidParameterException {
        List<User> usersFromDB = null;
        try {
            usersFromDB = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2, password)
                    .getResultList();
        } catch (PersistenceException e) {
            throw e;
        }
        if (usersFromDB == null) {
            throw new InvalidParameterException("Provided username or password is wrong");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("Database error");
        }

    }

    public void banUser(String username) throws PersistenceException, InvalidParameterException {
        User toBeBanned = getUser(username);
        toBeBanned.setBanned(true);
        updateProfile(toBeBanned);
    }

    public void updateProfile(User user) throws PersistenceException {
        em.merge(user);
    }
}
