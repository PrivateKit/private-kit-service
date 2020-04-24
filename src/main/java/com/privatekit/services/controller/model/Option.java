package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Option {

    @JsonProperty(required = true)
    private String key;

    @JsonProperty(required = true)
    private List<OptionValue> values;
}
