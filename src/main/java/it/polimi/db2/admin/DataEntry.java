package it.polimi.db2.admin;

import java.io.Serializable;
import java.util.Date;

public class DataEntry implements Serializable {
    private int id;
    private String name;
    private Date date;

    public DataEntry(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

}
