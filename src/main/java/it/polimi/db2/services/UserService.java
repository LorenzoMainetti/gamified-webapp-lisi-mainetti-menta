package it.polimi.db2.services;

import it.polimi.db2.entities.User;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.security.InvalidParameterException;
import java.util.List;

@Stateless

public class UserService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public UserService() {
    }

    //TODO da capire se è stato fatto bene
    public void insertUser(String username, String email, String password, boolean banned) throws PersistenceException, EJBTransactionRolledbackException{
        User user = new User();
        user.setBanned(banned);
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        try {
            em.persist(user);
        } catch (EJBTransactionRolledbackException | PersistenceException e) {
            throw e;
        }

    }

    public User getUser(String username) throws PersistenceException, InvalidParameterException {
        List<User> usersFromDB = em.createNamedQuery("User.getUser", User.class).setParameter(1, username)
                .getResultList();
        if (usersFromDB == null) {
            throw new InvalidParameterException("username or password is wrong");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    public User checkCredentials(String username, String password) throws PersistenceException, Exception {
        List<User> usersFromDB = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2, password)
                .getResultList();
        if (usersFromDB == null || usersFromDB.size()==0) {
            throw new Exception("Provided username or password is wrong");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new Exception("internal database error");
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
