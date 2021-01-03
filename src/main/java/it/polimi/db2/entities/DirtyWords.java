package it.polimi.db2.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "dirtyWords", schema = "db_gamified_app")
@NamedQuery(name = "DirtyWords.checkAnswer", query = "SELECT a FROM DirtyWords a WHERE a.word = ?1")
@NamedQuery(name = "DirtyWords.CheckSentence", query = "SELECT d FROM DirtyWords d WHERE d.word in ?1")
public class DirtyWords implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String word;

    private String language;

}
