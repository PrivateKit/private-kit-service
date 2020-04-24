package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCondition {
    private String response;

    @JsonProperty(value = "jump_to_key", required = true)
    private String jumpToKey;


    public static QuestionCondition create(String response, String jumpToKey) {
        final QuestionCondition questionCondition = new QuestionCondition();
        questionCondition.jumpToKey = jumpToKey;
        questionCondition.response = response;
        return questionCondition;

    }
}
