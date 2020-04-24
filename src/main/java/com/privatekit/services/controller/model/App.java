package com.privatekit.services.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class App {

    @JsonProperty(required = true)
    private String namespace;

    @JsonProperty(required = true)
    private String key;

    private String status;

    public static App create(String namespace, String key, String status)
    {
        final App app = new App();
        app.namespace= namespace;
        app.key=key;
        app.status=status;
        return app;
    }
}
