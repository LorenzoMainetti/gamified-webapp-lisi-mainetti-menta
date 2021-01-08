package it.polimi.db2.entities;

import it.polimi.db2.entities.ids.AnswerKey;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "answer", schema = "db_gamified_app")
@NamedQuery(name = "Answer.getUserFill", query = "SELECT distinct a.user FROM Answer a WHERE a.question.productId = ?1")
@NamedQuery(name = "Answer.getUserAnswers", query = "SELECT a FROM Answer a WHERE a.user = ?1 AND a.question.productId = ?2")
@NamedQuery(name = "Answer.getSpecificAnswer", query = "SELECT a FROM Answer a WHERE a.user = ?1 AND a.question = ?2 AND a.question.productId = ?3")
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AnswerKey id;

    private String text;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("questionKey")
    @JoinColumns({
            @JoinColumn(name = "questionId", referencedColumnName = "questionId"),
            @JoinColumn(name = "productId", referencedColumnName = "productId")
    })
    private Question question;


    public AnswerKey getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setId(AnswerKey id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


}