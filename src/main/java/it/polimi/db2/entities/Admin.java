package it.polimi.db2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "admin", schema = "db_gamified_app")
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")
public class Admin {
    private static final long serialVersionUID = 1L;

    @Id
    private String adminId;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @OneToMany
    private List<Product> products;
}
