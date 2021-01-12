package it.polimi.db2.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "product", schema = "db_gamified_app")
@NamedQuery(name = "Product.getProduct", query = "SELECT p FROM Product p  WHERE p.productId = ?1")
@NamedQuery(name = "Product.getProductDummy", query = "SELECT p FROM Product p WHERE p.name = ?1")
@NamedQuery(name = "Product.getProductOfTheDay", query = "SELECT p FROM Product p WHERE p.date = ?1")
@NamedQuery(name = "Product.getPastProducts", query = "SELECT p FROM Product p WHERE p.date < ?1 ORDER BY p.date")
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

    @NotNull
    private String creatorId;

    @Lob
    private byte[] image;

    //Product is OWNER entity (has fk column)
    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "adminId", insertable = false, updatable = false) //foreign key that references an admin tuple,
    private Admin creator;

    @ManyToMany(mappedBy = "products")
    private List<User> users;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, orphanRemoval=true) //amount of questions is limited
    private List<Question> questions;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE) //removing a product must cancel all its reviews
    private List <Reward> rewards;


    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setCreator(Admin creator) {
        this.creator = creator;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

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

    public String getCreatorId() {
        return creatorId;
    }

    public Admin getCreator() {
        return creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List <String> getQuestionsText() {

        List <String> texts = new LinkedList<>();
        questions.forEach( q -> texts.add(q.getText()) );
        return texts;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }
}

