package com.privatekit.server.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OptionValue {

    @JsonProperty(required = true)
    private String label;

    @JsonProperty(required = true)
    private String value;

    private String description;


    public static OptionValue create(String label, String value, String description) {
        final OptionValue optionValue = new OptionValue();
        optionValue.label = label;
        optionValue.value = value;
        optionValue.description = description;
        return optionValue;
    }

    public static OptionValue create(String l, String v) {
        return create(l, v, "");
    }
}
