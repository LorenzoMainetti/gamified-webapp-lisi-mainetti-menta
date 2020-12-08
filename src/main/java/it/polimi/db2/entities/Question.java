package it.polimi.db2.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "question", schema = "db_gamified_app")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    private int productId;

    private String text;
}
