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

    private QuestionKey questionKey;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public QuestionKey getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(QuestionKey questionKey) {
        this.questionKey = questionKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerKey answerKey = (AnswerKey) o;
        return Objects.equals(userId, answerKey.userId) && Objects.equals(questionKey, answerKey.questionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, questionKey);
    }
}
