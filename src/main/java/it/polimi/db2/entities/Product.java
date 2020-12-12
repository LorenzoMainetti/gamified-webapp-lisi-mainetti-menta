package it.polimi.db2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product", schema = "db_gamified_app")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    //auto-incremented id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @NotNull
    private String name;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String description;

    //Product is OWNER entity (has fk column)
    @ManyToOne 
    @JoinColumn(name = "creator_id", referencedColumnName = "admin_id") //foreign key that references an admin tuple,
    private Admin creator;

    @ManyToMany(mappedBy = "products")
    private List<User> users;



    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

