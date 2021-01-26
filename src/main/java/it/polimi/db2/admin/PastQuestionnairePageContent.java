package it.polimi.db2.admin;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class PastQuestionnairePageContent implements Serializable {

    List<DataEntry> pastQuestionnaires;

    public PastQuestionnairePageContent() {
        this.pastQuestionnaires = new LinkedList<>();
    }

    public List<DataEntry> getPastQuestionnaires() {
        return pastQuestionnaires;
    }
}
