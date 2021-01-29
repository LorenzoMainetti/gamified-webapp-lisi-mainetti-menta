package it.polimi.db2.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InspectionPageContent implements Serializable{
    private final String prodName, prodDescription, encodedImg;
    private final Date date;
    private final List<String> completed, canceled;
    private final Map<String, List<String>> answers;
    private final List<String> questions;

    public InspectionPageContent(List<String> completed, List<String> canceled, Map<String, List<String>> answers, List<String> questions,
                                     String prodName, String prodDescription, String encodedImg, Date date) {
        this.completed = completed;
        this.canceled = canceled;
        this.answers = answers;
        this.questions = questions;
        this.prodName = prodName;
        this.prodDescription = prodDescription;
        this.encodedImg = encodedImg;
        this.date = date;
    }
}
