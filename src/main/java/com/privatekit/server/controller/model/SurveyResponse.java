package com.privatekit.server.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SurveyResponse {

    @JsonProperty(value = "question_key",required = true)
    @NotEmpty(message = "question_key is missed")
    private int questionKey;

    @JsonProperty(value = "response",required = true)
    @NotEmpty(message = "response_value list is missed")
    private List<String> responseValue = new ArrayList<>();

    private boolean skipped;

    public static SurveyResponse create(int questionKey, List<String> responseValue, boolean skipped) {
        final SurveyResponse r = new SurveyResponse();
        r.questionKey = questionKey;
        r.responseValue.addAll(responseValue);
        r.skipped = skipped;
        return r;
    }
}
