package it.polimi.db2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "answer", schema = "db_gamified_app")
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String userId;

    @Id
    private int questionId;

    private String text;

}