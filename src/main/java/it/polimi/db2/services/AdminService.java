package it.polimi.db2.services;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;

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
     * @param product questionnaire to create
     * @return true if the date is in the future and no other questionnaire are already present, false otherwise
     * @throws InvalidParameterException if there is a consistency problem with the DB
     */
    public boolean checkQuestionnaireValidity(Product product) throws InvalidParameterException{
        if(product.getDate().before(java.sql.Date.valueOf(LocalDate.now()))){
            return false;
        }
        else{
            List<Product> products = em.createNamedQuery("Product.getProductOfTheDay", Product.class).setParameter(1, product.getDate()).getResultList();
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

    /*
    1) create new questionnaire => productService
    2) inspect past questionnaire (users who submitted, users who cancelled, answers of each user)
    3) delete past questionnaire
     */

}
