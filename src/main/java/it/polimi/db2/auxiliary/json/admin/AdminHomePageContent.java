package it.polimi.db2.auxiliary.json.admin;

import it.polimi.db2.auxiliary.AdminStatus;

import java.io.Serializable;

public class AdminHomePageContent implements Serializable {
    private final String adminId, email;
    private final String prodName, prodDescription, encodedImg;
    private final AdminStatus adminStatus;

    public AdminHomePageContent(String adminId, String email, String prodName, String prodDescription, String encodedImg, AdminStatus adminStatus) {
        this.adminId = adminId;
        this.email = email;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.encodedImg = encodedImg;
        this.adminStatus = adminStatus;
    }
}
