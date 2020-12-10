package it.polimi.db2.entities.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AnswerKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "product_id")
    private int productId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerKey answerKey = (AnswerKey) o;
        return questionId == answerKey.questionId &&
                productId == answerKey.productId &&
                Objects.equals(userId, answerKey.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, questionId, productId);
    }

}
