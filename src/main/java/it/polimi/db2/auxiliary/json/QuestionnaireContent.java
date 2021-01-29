package it.polimi.db2.auxiliary.json;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;


public class QuestionnaireContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name, description;
    private final List<String> optionalQuestions;

    public QuestionnaireContent(String name, String description, List<String> optional_Questions) throws InvalidParameterException {
        this.name = name;
        this.description = description;
        this.optionalQuestions = optional_Questions;
    }
}
