package it.polimi.db2.admin;

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

    //Admin gets only the data and product name for a questionnaire (LAZY) in the homepage
    //he can access further information by clicking to a questionnaire
    Map<Date, String> pastQuestionnaires = new LinkedHashMap<>();

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


    public Map<Date, String> getPastQuestionnaires() {
        return pastQuestionnaires;
    }

    public void setPastQuestionnaires(Map<Date, String> pastQuestionnaires) {
        this.pastQuestionnaires = pastQuestionnaires;
    }
}
