package it.polimi.db2.entities;

import it.polimi.db2.entities.ids.QuestionKey;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@IdClass(QuestionKey.class)
@Table(name = "question", schema = "db_gamified_app")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    @Id
    private int productId;

    private String text;

    @OneToOne
    @PrimaryKeyJoinColumn(name="product_id", referencedColumnName="product_id")
    private Product product;

    @ManyToMany
    @JoinTable(name="answer",
            joinColumns={@JoinColumn(name="question_id", referencedColumnName = "question_id"),
                         @JoinColumn(name="product_id", referencedColumnName = "product_id")},
            inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> users;

}
