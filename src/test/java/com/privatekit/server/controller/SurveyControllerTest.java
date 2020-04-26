package com.privatekit.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.privatekit.server.controller.model.*;
import com.privatekit.server.repository.SurveyRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;

import static org.assertj.core.util.Lists.list;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebMvc
public class SurveyControllerTest {

    //~ Instance Fields ..............................................................................................................................
    private MockMvc mockMvc;
    @Autowired private SurveyRepository surveyRepository;

    @BeforeEach
    void initValues() {

        mockMvc             = standaloneSetup(surveyController).build();

        final com.privatekit.server.entity.Survey s = new com.privatekit.server.entity.Survey();
        s.setAppKey("abcdefg");
        s.setName("Symptoms Checker Survey");
        s.setDescription("Symptoms Checker Survey");
        s.setAppNamespace("myNameSpace");

        surveyRepository.save(s);

    }

    @Autowired
    private SurveyController surveyController;

    //~ Methods ......................................................................................................................................

    @Test
    void testGetSurveys() throws Exception
    {
        mockMvc.perform(get("/v1.0/myNameSpace/survey"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
    }

    @Test
    void testGetSurveysFromInvalidNamespace() throws Exception
    {

        com.privatekit.server.entity.Survey s = new com.privatekit.server.entity.Survey();
        s.setAppKey("s");
        s.setName("3");
        s.setDescription("dd");
        s.setAppNamespace("myNameSpace");

        surveyRepository.save(s);

        mockMvc.perform(get("/v1.0/none/survey"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testPostSurvey() throws Exception
    {

        assertTrue(surveyRepository.findByAppNamespace("anotherNamespace").isEmpty());

        mockMvc.perform(post("/v1.0/anotherNamespace/survey").content(asJsonString(createMockSurvey()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk());

        final Collection<com.privatekit.server.entity.Survey> list = surveyRepository.findByAppNamespace("anotherNamespace");

        assertFalse(list.isEmpty());

        assertEquals(1, list.size());
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

        final com.privatekit.server.entity.Survey survey = surveyRepository.findByAppNamespace("myNameSpace").iterator().next();
        final SurveyResponse response = SurveyResponse.create(1234, list("Great"), true);

        mockMvc.perform(post(String.format("/v1.0/%s/survey/%d/response","myNameSpace", survey.getId()))
                .content(asJsonString(Lists.list(response)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testPostSurveyResponseSurveyNotFound() throws Exception
    {

        final SurveyResponse response = SurveyResponse.create(1234, list("Great"), true);

        mockMvc.perform(post("/v1.0/1/survey/1/response")
                .content(asJsonString(Lists.list(response)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isNotFound());
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