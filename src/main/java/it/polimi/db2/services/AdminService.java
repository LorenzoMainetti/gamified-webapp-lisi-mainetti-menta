package it.polimi.db2.services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class AdminService {

    @PersistenceContext(unitName = "gamifiedApp")
    private EntityManager em;

    public AdminService() { }

    /*
    1) create new questionnaire
    2) inspect past questionnaire (users who submitted, users who cancelled, answers of each user)
    3) delete past questionnaire
     */
}
