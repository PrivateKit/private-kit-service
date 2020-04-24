package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SurveyList {

    @JsonProperty(required = true)
    private List<Survey> data = new ArrayList<>();

    public SurveyList addSurvey(Survey survey) {
        if (survey != null) data.add(survey);
        return this;
    }

    public List<Survey> getData() { return data;}
}
