package it.polimi.db2.entities.ids;

import jakarta.persistence.Column;

import java.io.Serializable;

public class QuestionKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "product_id")
    private int productId;

}
