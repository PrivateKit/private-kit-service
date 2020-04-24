package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SurveyResponse {

    @JsonProperty(value = "question_id",required = true)
    private String questionId;

    @JsonProperty(value = "response_value",required = true)
    private List<String> responseValue = new ArrayList<>();

    private boolean skkiped;

    public static SurveyResponse create(String questionId, List<String> responseValue, boolean skkiped) {
        final SurveyResponse r = new SurveyResponse();
        r.questionId = questionId;
        r.responseValue.addAll(responseValue);
        r.skkiped = skkiped;
        return r;
    }
}
