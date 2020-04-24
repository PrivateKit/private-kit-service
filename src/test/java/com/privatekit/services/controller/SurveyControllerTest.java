package com.privatekit.services.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.privatekit.services.controller.model.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.util.Lists.list;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureWebMvc
public class SurveyControllerTest {

    //~ Instance Fields ..............................................................................................................................
    private MockMvc mockMvc;

    @BeforeEach
    void initValues() {

        mockMvc             = standaloneSetup(surveyController).build();
    }

    @Autowired
    private SurveyController surveyController;

    //~ Methods ......................................................................................................................................

    @Test
    void testGetSurveys() throws Exception
    {
        mockMvc.perform(get("/v1.0/none/survey"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
    }

    @Test
    void testPostSurvey() throws Exception
    {

        mockMvc.perform(post("/v1.0/none/survey").content(asJsonString(createMockSurvey()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testPostSurveyBadRequest() throws Exception
    {
        mockMvc.perform(post("/v1.0/none/survey").content(asJsonString(new Survey()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostSurveyResponse() throws Exception
    {

        final SurveyResponse response = SurveyResponse.create("12345", list("Great"), true);

        mockMvc.perform(post("/v1.0/1/survey/1/response")
                .content(asJsonString(Lists.list(response)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Survey createMockSurvey() {
        final Survey survey = new Survey();
        survey.setName("Symptoms Checker Survey");
        survey.setDescription("Symptoms Checker Survey");
        final Question q1 = new Question();

        q1.setQuestionKey("1");
        q1.setQuestionText("Select your symptoms?");
        q1.setQuestionType("MULTI");
        q1.setRequired(true);
        q1.setScreenType("Checkbox");
        q1.setOptionKey("option_1");

        q1.getConditions().addAll(com.google.inject.internal.util.Lists.newArrayList(QuestionCondition.create("Y", "3"), QuestionCondition.create("N", "2")));


        final Question q2 = new Question();
        q2.setQuestionKey("2");
        q2.setQuestionType("END");

        final Question q3 = new Question();
        q3.setQuestionKey("3");
        q3.setQuestionType("END");

        survey.getQuestions().add(q1);
        survey.getQuestions().add(q2);
        survey.getQuestions().add(q3);

        final Option opt1 = new Option();
        opt1.setKey("option_1");
        opt1.setValues(com.google.inject.internal.util.Lists.newArrayList(OptionValue.create("Yes", "Y"), OptionValue.create("No", "N"), OptionValue.create("Maybe", "M")));

        survey.getOptions().add(opt1);

        survey.getScreenTypes().put("Checkbox", "911");

        return  survey;
    }
}