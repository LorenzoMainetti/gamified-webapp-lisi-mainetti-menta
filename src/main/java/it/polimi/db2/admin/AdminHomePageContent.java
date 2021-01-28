package it.polimi.db2.admin;

import it.polimi.db2.auxiliary.AdminStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminHomePageContent implements Serializable {

    //personal
    String adminId, email;

    //potd
    String prodName, prodDescription, encodedImg;

    AdminStatus adminStatus;

    public AdminHomePageContent(String adminId, String email, String prodName, String prodDescription, String encodedImg, AdminStatus adminStatus) {
        this.adminId = adminId;
        this.email = email;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.encodedImg = encodedImg;
        this.adminStatus = adminStatus;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getEncodedImg() {
        return encodedImg;
    }

    public void setEncodedImg(String encodedImg) {
        this.encodedImg = encodedImg;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(AdminStatus adminStatus) {
        this.adminStatus = adminStatus;
    }
}
