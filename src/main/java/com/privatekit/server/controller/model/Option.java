package com.privatekit.server.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Option {

    @JsonProperty(required = true)
    private String key;

    @JsonProperty(required = true)
    private List<OptionValue> values = new ArrayList<>();
}
