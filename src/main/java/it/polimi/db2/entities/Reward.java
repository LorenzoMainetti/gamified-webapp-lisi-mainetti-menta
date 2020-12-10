package it.polimi.db2.entities;

import it.polimi.db2.entities.ids.RewardKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "reward", schema = "db_gamified_app")
public class Reward implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RewardKey id;

    @NotNull
    private int points;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

}
