package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class Survey {

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
    private Map<String, String> screenTypes = new HashMap<>();
}
