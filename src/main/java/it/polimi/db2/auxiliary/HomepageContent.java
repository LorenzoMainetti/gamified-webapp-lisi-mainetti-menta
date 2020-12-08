package it.polimi.db2.auxiliary;

import java.io.Serializable;

public class HomepageContent implements Serializable {
    String username;
    boolean admin;
    public HomepageContent(String username) {
        this.username = username;
        this.admin = false;
    }
}
