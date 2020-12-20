package it.polimi.db2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

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

    //other side of 1:1 relationship with owner 'Product' that maps admin <> product
    //Fetch = lazy is fine because we don't always need created product (es. during login or on creation)
    @OneToMany(mappedBy = "creator")
    private Set<Product> createdProducts; //no need to return a specific order

}
