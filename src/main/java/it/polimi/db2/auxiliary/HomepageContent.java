package it.polimi.db2.auxiliary;

import it.polimi.db2.entities.Product;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class HomepageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    String username;
    boolean admin;

    String prodName, prodDescription;
    BufferedImage prodImage;
    String imgPath;

    public HomepageContent(String username) {
        this.username = username;
        this.admin = false;
    }

    public HomepageContent(String username, boolean admin, String prodName, String prodDescription, String imgPath) {
        this.username = username;
        this.admin = admin;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        //this.prodImage = prodImage;
        this.imgPath = imgPath;
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

    public BufferedImage getProdImage() {
        return prodImage;
    }

    public void setProdImage(BufferedImage prodImage) {
        this.prodImage = prodImage;
    }
}
