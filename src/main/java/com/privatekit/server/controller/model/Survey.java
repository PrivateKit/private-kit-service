package com.privatekit.server.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Survey {

    private int id;

    @JsonProperty(required = true)
    @NotEmpty(message = "name is missed")
    private String name;

    @JsonProperty(required = true)
    @NotEmpty(message = "description is missed")
    private String description;

    @JsonProperty
    private String image;

    @JsonProperty(required = true)
    @NotEmpty(message = "questions list is missed")
    private List<Question> questions = new ArrayList<>();

    @JsonProperty(required = true)
    @NotEmpty(message = "options list is missed")
    private List<Option> options = new ArrayList<>();

    @JsonProperty(value="screen_types", required = true)
    @NotEmpty(message = "screen_types are missed")
    private List<String> screenTypes = new ArrayList<>();

    public static Survey from(com.privatekit.server.entity.Survey s) {

        final Survey survey = new Survey();
        survey.setId(s.getId());
        survey.setName(s.getName());
        survey.setDescription(s.getDescription());
        survey.setImage(s.getImage());

        return survey;
    }
}
