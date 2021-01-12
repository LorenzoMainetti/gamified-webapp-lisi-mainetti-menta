package it.polimi.db2.admin;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;




public class PastQuestionnairePageContent implements Serializable {

    static class DataEntry implements Serializable {

        public DataEntry(int id, String name, Date date) {
            this.id = id;
            this.name = name;
            this.date = date;
        }

        int id;
        String name;
        Date date;

    }

    List<DataEntry> pastQuestionnaires;

    public PastQuestionnairePageContent() {
        this.pastQuestionnaires = new LinkedList<>();
    }

    public void addEntry(int id, String name, Date date) {
        pastQuestionnaires.add(new DataEntry(id, name, date));
    }

    public String getJsonRepresentation() {
        return new Gson().toJson(this);
    }
}
