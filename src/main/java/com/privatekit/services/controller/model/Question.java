package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Question {

    @JsonProperty(value = "question_key", required = true)
    private String questionKey;

    @JsonProperty(value = "question_text", required = true)
    private String questionText;

    @JsonProperty
    private String image;

    @JsonProperty(value = "question_type", required = true)
    private String questionType;

    private boolean required = false;

    @JsonProperty(value = "screen_type", required = true)
    private String screenType;

    @JsonProperty(value = "option_key", required = true)
    private String optionKey;

    @JsonProperty(value = "uitype")
    private String uiType;

    @JsonProperty(required = true)
    private List<QuestionCondition> conditions = new ArrayList<>();

}
