package it.polimi.db2.auxiliary;

import it.polimi.db2.entities.Product;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class HomepageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private UserStatus userStatus;
    private boolean admin;

    private String prodName, prodDescription, encodedImg;
    private ArrayList<String> reviews;
    private byte[] img;

    public HomepageContent(String username) {
        this.username = username;
        this.admin = false;
    }

    public HomepageContent(String username, boolean admin, String prodName, String prodDescription, byte[] img, String encodedImg, ArrayList<String> reviews, UserStatus userStatus) {
        this.username = username;
        this.admin = admin;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.img = img;
        this.reviews = reviews;
        this.encodedImg = encodedImg;
        this.userStatus = userStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDescription() {
        return prodDescription;
    }

    public void setProdDescription(String prodDescription) {
        this.prodDescription = prodDescription;
    }
}
