package it.polimi.db2.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Table(name = "reward", schema = "db_gamified_app")
public class Reward implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RewardKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private int points;

}
