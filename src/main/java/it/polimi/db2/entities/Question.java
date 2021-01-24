package it.polimi.db2.entities;

import it.polimi.db2.entities.ids.QuestionKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    private boolean isMandatory;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="productId", referencedColumnName="productId")
    private Product product;

    @ManyToMany(cascade = CascadeType.REMOVE) //maybe orphan removal to delete answers too
    @JoinTable(name="answer",
            joinColumns={@JoinColumn(name="questionId", referencedColumnName = "questionId"),
                         @JoinColumn(name="productId", referencedColumnName = "productId")},
            inverseJoinColumns={@JoinColumn(name="userId")})
    private Set<User> users;

    private int questionNumber;

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

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

    public QuestionKey getQuestionKey() {
        return new QuestionKey(questionId, productId);
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

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
