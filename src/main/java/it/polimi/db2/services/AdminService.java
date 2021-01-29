package it.polimi.db2.services;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Stateless
public class AdminService {
    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public AdminService() { }

    /**
     * Method to obtain admin reference from id
     * @param adminId id of the admin
     * @return admin instance
     * @throws PersistenceException if there is a persistence problem with the DB
     * @throws InvalidParameterException if there is a consistency problem with the DB
     */
    public Admin getAdmin(String adminId) throws PersistenceException, InvalidParameterException {
        Admin adminFromDB = em.find(Admin.class, adminId);
        if (adminFromDB == null)
            throw new InvalidParameterException("internal database error");
        else
            return adminFromDB;
    }

    /**
     * Method to check if there exist an admin with these credentials
     * @param adminId id of the admin
     * @param password password of the admin
     * @return admin instance if exist
     * @throws InvalidParameterException if there is no admin with these credentials
     */
    public Admin checkAdminCredentials(String adminId, String password) throws InvalidParameterException{
        List<Admin> admins = em.createNamedQuery("Admin.checkCredentials", Admin.class).setParameter(1, adminId).setParameter(2, password)
                .getResultList();
        if (admins == null || admins.isEmpty()) {
            throw new InvalidParameterException("Provided username or password is wrong");
        }
        else if(admins.size()==1) {
            return admins.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to check if a questionnaire date is usable
     * @param date date of the questionnaire to create
     * @return true if the date is in the future and no other questionnaire are already present, false otherwise
     * @throws InvalidParameterException if there is a consistency problem with the DB
     */
    public boolean checkQuestionnaireValidity(Date date) throws InvalidParameterException{
        if(date.before(java.sql.Date.valueOf(LocalDate.now()))){
            return false;
        }
        else{
            List<Product> products = em.createNamedQuery("Product.getProductOfTheDay", Product.class).setParameter(1, date).getResultList();
            if (products == null || products.isEmpty()) {
                return true;
            }
            else if(products.size()==1) {
                return false;
            }
            else {
                throw new InvalidParameterException("internal database error");
            }
        }
    }
}
