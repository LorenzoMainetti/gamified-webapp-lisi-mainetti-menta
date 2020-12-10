package it.polimi.db2.entities;

import it.polimi.db2.entities.ids.AnswerKey;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "answer", schema = "db_gamified_app")
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AnswerKey id;

    private String text;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumns({
            @JoinColumn(name = "question_id", referencedColumnName = "question_id"),
            //TODO check insertable and updatable
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
    })
    private Question question;

}