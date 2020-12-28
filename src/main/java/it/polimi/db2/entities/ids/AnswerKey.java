package it.polimi.db2.entities.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AnswerKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "userId")
    private String userId;

    @Column(name = "questionId")
    private int questionId;

    @Column(name = "productId")
    private int productId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

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
