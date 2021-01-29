package it.polimi.db2.auxiliary.json;

import it.polimi.db2.auxiliary.UserStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class HomepageContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final UserStatus userStatus;
    private final boolean admin;


    private final String prodName, prodDescription, encodedImg;
    private final ArrayList<String> reviews;

    public HomepageContent(String username, boolean admin, String prodName, String prodDescription, String encodedImg, ArrayList<String> reviews, UserStatus userStatus) {
        this.username = username;
        this.admin = admin;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.reviews = reviews;
        this.encodedImg = encodedImg;
        this.userStatus = userStatus;
    }
}
