package it.polimi.db2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "admin", schema = "db_gamified_app")
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
    @OneToMany(mappedBy = "creator", cascade = CascadeType.PERSIST)
    private Set<Product> createdProducts; //no need to return a specific order

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Product> getCreatedProducts() {
        return createdProducts;
    }

    public void setCreatedProducts(Set<Product> createdProducts) {
        this.createdProducts = createdProducts;
    }

    public String getAdminId() {
        return adminId;
    }

    public void removeCreatedProduct(Product product) {
        createdProducts.remove(product);
    }

}
