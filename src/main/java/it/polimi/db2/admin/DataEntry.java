package it.polimi.db2.admin;

import java.io.Serializable;
import java.util.Date;

public class DataEntry implements Serializable {
    private final int id;
    private final String name;
    private final Date date;

    public DataEntry(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }
}
