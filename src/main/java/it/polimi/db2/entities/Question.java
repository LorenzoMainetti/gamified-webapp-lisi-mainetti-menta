package it.polimi.db2.entities;

import it.polimi.db2.entities.ids.QuestionKey;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@IdClass(QuestionKey.class)
@Table(name = "question", schema = "db_gamified_app")
@NamedQuery(name = "Question.getQuestion", query = "SELECT q FROM Question q  WHERE q.questionId = ?1 AND q.productId = ?2")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    @Id
    private int productId;

    private String text;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="product_id", referencedColumnName="product_id")
    private Product product;

    @ManyToMany
    @JoinTable(name="answer",
            joinColumns={@JoinColumn(name="question_id", referencedColumnName = "question_id"),
                         @JoinColumn(name="product_id", referencedColumnName = "product_id")},
            inverseJoinColumns={@JoinColumn(name="user_id")})
    private Set<User> users;

    public boolean isStatQuestion(){
        return false;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getProductId() {
        return productId;
    }

    public String getText() {
        return text;
    }

    public Product getProduct() {
        return product;
    }

    public Set<User> getUsers() {
        return users;
    }
}
