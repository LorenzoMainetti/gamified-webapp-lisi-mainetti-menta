package services;

import entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.Random;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "gamifiedapp")
    private EntityManager em;

    public UserService() {
    }

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
    /*
    public User checkCredentials(String usrn, String pwd) throws NonUniqueResultException {
        List<User> uList = null;
        try {
            uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
                    .getResultList();
        } catch (PersistenceException e) {
            //TODO throw new CredentialsException("Could not verify credentals");
        }
        if (uList.isEmpty())
            return null;
        else if (uList.size() == 1)
            return uList.get(0);
        throw new NonUniqueResultException("More than one user registered with same credentials");

    } */
/*
    public void updateProfile(User u) throws UpdateProfileException {
        try {
            em.merge(u);
        } catch (PersistenceException e) {
            throw new UpdateProfileException("Could not change profile");
        }
    } */
}
