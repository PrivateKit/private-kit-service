package com.privatekit.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.privatekit.server.controller.model.*;
import com.privatekit.server.controller.model.Question;
import com.privatekit.server.controller.model.QuestionCondition;
import com.privatekit.server.controller.model.Survey;
import com.privatekit.server.controller.model.SurveyResponse;
import com.privatekit.server.entity.*;
import com.privatekit.server.repository.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.inject.internal.util.Lists.newArrayList;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureWebMvc
public class SurveyControllerTest {

    //~ Instance Fields ..............................................................................................................................
    private MockMvc mockMvc;

    @Autowired private SurveyRepository surveyRepository;

    @Autowired private QuestionRepository questionRepository;
    @Autowired private QuestionConditionRepository questionConditionRepository;
    @Autowired private SurveyOptionGroupRepository surveyOptionGroupRepository;
    @Autowired private OptionRepository optionRepository;

    @Autowired private SurveyController surveyController;

    @BeforeEach
    void initValues() {

        mockMvc             = standaloneSetup(surveyController).build();

        final com.privatekit.server.entity.Survey s = new com.privatekit.server.entity.Survey();
        s.setAppKey("abcdefg");
        s.setName("Symptoms Checker Survey");
        s.setDescription("Symptoms Checker Survey");
        s.setAppNamespace("myNameSpace");

        Integer surveyId= surveyRepository.save(s).getId();

        final com.privatekit.server.entity.Question q1 = new com.privatekit.server.entity.Question();
        QuestionId id = new QuestionId();
        id.setSurveyId(surveyId);
        id.setQuestionKey("1");
        q1.setOptionKey("option_1");
        q1.setType("MULTI");
        q1.setText("Select your symptoms?");
        q1.setRequired(true);
        q1.setScreenType("Checkbox");
        q1.setId(id);

        questionRepository.save(q1);

        insertQuestionCondition(surveyId, "Y", "3", q1.getOptionKey());
        insertQuestionCondition(surveyId, "N", "2", q1.getOptionKey());

        createSurveyOption(surveyId, q1.getOptionKey(),
                                Lists.list( Lists.list("Y", "Yes"),
                                            Lists.list("N", "No"),
                                            Lists.list("M", "Maybe")));

    }

    private void createSurveyOption(Integer surveyId, String optionKey, List<List<String>> list) {
        final SurveyOption surveyOption = new SurveyOption();
        final SurveyOptionId id1 = new SurveyOptionId();
        id1.setOptionKey(optionKey);
        id1.setSurveyId(surveyId);
        surveyOption.setId(id1);

        surveyOption.setValues(list.stream().map(i->{
            final SurveyOptionValue e = new SurveyOptionValue();
            e.setOptionValue(i.get(0));
            e.setOptionLabel(i.get(1));
            e.setOption(surveyOption);
            return e;
        }).collect(Collectors.toSet()));

        optionRepository.save(surveyOption);
    }

    private void insertQuestionCondition(Integer surveyId, String response,  String jumpToKey, String questionKey) {
        final com.privatekit.server.entity.QuestionCondition qc = new com.privatekit.server.entity.QuestionCondition();
        final QuestionConditionId id = new QuestionConditionId();
        id.setSurveyId(surveyId);
        id.setQuestionKey(questionKey);
        id.setResponse(response);
        qc.setJumpToKey(jumpToKey);
        qc.setId(id);
        questionConditionRepository.save(qc);
    }


    //~ Methods ......................................................................................................................................

    @Test
    void testGetSurveys() throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(get("/v1.0/myNameSpace/survey"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists()).andReturn();

        final String contentAsString = mvcResult.getResponse().getContentAsString();

        final SurveyList surveyList = fromJsonString(contentAsString, SurveyList.class);
        assertFalse(surveyList.getData().isEmpty());

        final Survey survey = surveyList.getData().get(0);

        assertEquals("Symptoms Checker Survey", survey.getName());
        assertEquals("Symptoms Checker Survey", survey.getDescription());
        assertFalse(survey.getQuestions().isEmpty());

        final Question question = survey.getQuestions().get(0);

        assertFalse(question.getConditions().isEmpty());

        assertTrue(survey.getOptions().isEmpty());
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

    static <T> T fromJsonString(final String obj, Class<T> tr) {
        try {
            return new ObjectMapper().readValue(obj,tr);
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

        q1.getConditions().addAll(newArrayList(QuestionCondition.create("Y", "3"), QuestionCondition.create("N", "2")));

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
        opt1.setValues(newArrayList(OptionValue.create("Yes", "Y"), OptionValue.create("No", "N"), OptionValue.create("Maybe", "M")));

        survey.getOptions().add(opt1);

        survey.getScreenTypes().put("Checkbox", "911");

        return  survey;
    }
}
