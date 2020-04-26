package com.privatekit.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class CaptchaControllerTest {

    @Test
    public void testResponse() throws Exception {

        // TODO: Validate response content has updated values
        // This is not possible with the current format of the test because content() started returning an empty string
        // after initializing the mock mvc with a standaloneSetup
        // However, the standaloneSetup is required to bypass Filters and any other stuff that is not required
        // in this context

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");

        MockMvcBuilders
                .standaloneSetup(new CaptchaController())
                .setViewResolvers(resolver)
                .build()
                .perform(get("/v1.0/captcha"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("captcha"));
    }
}
