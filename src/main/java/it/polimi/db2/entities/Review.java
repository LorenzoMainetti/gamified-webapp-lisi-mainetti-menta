package it.polimi.db2.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "review", schema = "db_gamified_app")
@NamedQuery(name = "Review.getReviewById", query = "SELECT r FROM Review r WHERE r.idReview = ?1")
@NamedQuery(name = "Review.getReviewNumber", query = "SELECT COUNT(r) FROM Review r")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReview;

    private String text;

    public int getIdReview() {
        return idReview;
    }

    public String getText() {
        return text;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public void setText(String text) {
        this.text = text;
    }
}
