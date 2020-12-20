package it.polimi.db2.auxiliary;

import it.polimi.db2.entities.Product;

import java.awt.*;
import java.io.Serializable;

public class HomepageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    String username;
    boolean admin;

    String prodName, prodDescription;
    Image  prodImage;

    public HomepageContent(String username) {
        this.username = username;
        this.admin = false;
    }

    public HomepageContent(String username, boolean admin, String prodName, String prodDescription, Image prodImage) {
        this.username = username;
        this.admin = admin;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.prodImage = prodImage;
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

    public Image getProdImage() {
        return prodImage;
    }

    public void setProdImage(Image prodImage) {
        this.prodImage = prodImage;
    }
}
