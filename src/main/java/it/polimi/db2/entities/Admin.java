package it.polimi.db2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin", schema = "db_gamified_app")
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")
public class Admin {
    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    private String email;

    private String password;
}
