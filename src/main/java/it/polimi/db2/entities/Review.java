package it.polimi.db2.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "review", schema = "db_gamified_app")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReview;

    private String text;

}
