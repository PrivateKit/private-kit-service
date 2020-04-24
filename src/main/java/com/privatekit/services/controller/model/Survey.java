package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class Survey {

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String description;

    @JsonProperty
    private String image;

    @JsonProperty(required = true)
    private List<Question> questions = new ArrayList<>();

    @JsonProperty(required = true)
    private List<Option> options = new ArrayList<>();

    @JsonProperty(value="screen_types", required = true)
    private Map<String, String> screenTypes = new HashMap<>();
}
