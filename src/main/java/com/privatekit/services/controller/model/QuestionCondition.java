package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class QuestionCondition {

    @JsonProperty(required = true)
    @NotEmpty(message = "response is missed")
    private String response;

    @JsonProperty(value = "jump_to_key", required = true)
    @NotEmpty(message = "jump_to_key is missed")
    private String jumpToKey;


    public static QuestionCondition create(String response, String jumpToKey) {
        final QuestionCondition questionCondition = new QuestionCondition();
        questionCondition.jumpToKey = jumpToKey;
        questionCondition.response = response;
        return questionCondition;

    }
}
