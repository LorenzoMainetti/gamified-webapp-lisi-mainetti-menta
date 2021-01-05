package it.polimi.db2.services;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;
import java.util.List;

@Stateless
public class AdminService {

    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public AdminService() { }

    public Admin getAdmin(String adminId) throws PersistenceException, InvalidParameterException {
        List<Admin> adminFromDB = em.createNamedQuery("Admin.getAdmin", Admin.class).setParameter(1, adminId)
                .getResultList();
        if (adminFromDB == null) {
            throw new InvalidParameterException("username or password is wrong");
        }
        else if(adminFromDB.size()==1) {
            return adminFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /*
    1) create new questionnaire => productService
    2) inspect past questionnaire (users who submitted, users who cancelled, answers of each user)
    3) delete past questionnaire
     */
}
