package it.polimi.db2.admin;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class InspectionPageContent implements Serializable{

        List<String> completed, canceled;
        Map<String, List<String>> answers;
        List<String> questions;

        public InspectionPageContent(List<String> completed, List<String> canceled, Map<String, List<String>> answers, List<String> questions) {
            this.completed = completed;
            this.canceled = canceled;
            this.answers = answers;
            this.questions = questions;
        }

}
