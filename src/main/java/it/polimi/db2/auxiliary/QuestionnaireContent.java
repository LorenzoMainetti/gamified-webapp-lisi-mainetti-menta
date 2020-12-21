package it.polimi.db2.auxiliary;

import com.google.gson.Gson;

import java.awt.*;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


public class QuestionnaireContent implements Serializable {

    private static final long serialVersionUID = 1L;

    String name, description;
    List<String> optional_Questions = new ArrayList<String>();

    //Todo find a way to send an Image image;

    public QuestionnaireContent(String name, String description, List<String> optional_Questions) throws InvalidParameterException {

        this.name = name;
        this.description = description;
        this.optional_Questions = optional_Questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getOptional_Questions() {
        return optional_Questions;
    }

    public void setOptional_Questions(List<String> optional_Questions) {
        this.optional_Questions = optional_Questions;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
