package it.polimi.db2.services;

import it.polimi.db2.entities.Admin;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;

@Stateless
public class AdminService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public AdminService() { }

    public Admin getAdmin(String adminId) throws PersistenceException, InvalidParameterException {
        Admin adminFromDB = em.find(Admin.class, adminId);
        if (adminFromDB == null)
            throw new InvalidParameterException("internal database error");
        else
            return adminFromDB;
    }



    /*
    1) create new questionnaire => productService
    2) inspect past questionnaire (users who submitted, users who cancelled, answers of each user)
    3) delete past questionnaire
     */

}
