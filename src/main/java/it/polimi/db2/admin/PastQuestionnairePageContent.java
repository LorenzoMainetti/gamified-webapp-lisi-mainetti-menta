package it.polimi.db2.admin;

import java.util.Date;

public class PastQuestionnairePageContent {
    int prodId;
    String prodName, prodDescription, encodedImg;
    Date prodDate;

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int id) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Date getProdDate() { return prodDate; }

    public void setProdDate(Date prodDate) { this.prodDate = prodDate; }

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

}
