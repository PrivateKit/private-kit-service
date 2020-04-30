package com.privatekit.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.privatekit.server.controller.model.Question;
import com.privatekit.server.controller.model.QuestionCondition;
import com.privatekit.server.controller.model.Survey;
import com.privatekit.server.controller.model.SurveyResponse;
import com.privatekit.server.controller.model.*;
import com.privatekit.server.entity.App;
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


import java.util.List;
import java.util.stream.Collectors;

import static com.google.inject.internal.util.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
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
    @Autowired private AppRepository appRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private QuestionConditionRepository questionConditionRepository;
    @Autowired private SurveyScreenTypeRepository surveyScreenTypeRepository;
    @Autowired private ScreenTypeRepository screenTypeRepository;
    @Autowired private OptionRepository optionRepository;

    @Autowired private SurveyController surveyController;
    private int surveyId;

    @BeforeEach
    void initValues() {

        mockMvc             = standaloneSetup(surveyController).build();

        createApp("123456", "1234");
        createApp("4567", "4567");

        final com.privatekit.server.entity.Survey s = new com.privatekit.server.entity.Survey();
        s.setAppKey("123456");
        s.setName("Symptoms Checker Survey");
        s.setDescription("Symptoms Checker Survey");
        s.setAppNamespace("1234");

        surveyId= surveyRepository.save(s).getId();

        final com.privatekit.server.entity.Question q1 = new com.privatekit.server.entity.Question();
        QuestionId id = new QuestionId();
        id.setSurveyId(surveyId);
        id.setQuestionKey("1");
        q1.setOptionKey("option_1");
        q1.setType("MULTI");
        q1.setText("Select your symptoms?");
        q1.setDescription("Hello World");
        q1.setTitle("Welcome");
        q1.setRequired(true);
        q1.setScreenType("Checkbox");
        q1.setId(id);

        questionRepository.save(q1);

        insertQuestionCondition(surveyId, "Y", "3", q1.getId().getQuestionKey());
        insertQuestionCondition(surveyId, "N", "2", q1.getId().getQuestionKey());

        createSurveyOption(surveyId, q1.getOptionKey(),
                                Lists.list( Lists.list("Y", "Yes"),
                                            Lists.list("N", "No"),
                                            Lists.list("M", "Maybe")));


        final ScreenType screenType1 = new ScreenType();
        screenType1.setId("911");
        final ScreenType screenType2 = new ScreenType();
        screenType2.setId("Checkbox");
        final SurveyScreenTypeId id1 = new SurveyScreenTypeId();
        id1.setSurveyId(surveyId);
        id1.setScreenTypeKey("911");
        final SurveyScreenType surveyScreenType1 = new SurveyScreenType();
        surveyScreenType1.setId(id1);
        final SurveyScreenTypeId id2 = new SurveyScreenTypeId();
        id2.setSurveyId(surveyId);
        id2.setScreenTypeKey("Checkboox");
        final SurveyScreenType surveyScreenType2 = new SurveyScreenType();
        surveyScreenType2.setId(id2);
        screenTypeRepository.save(screenType1);
        screenTypeRepository.save(screenType2);
        surveyScreenTypeRepository.save(surveyScreenType1);
        surveyScreenTypeRepository.save(surveyScreenType2);

    }

    private void createApp(String key, String namespace) {
        final App app = new App();
        AppId appId = new AppId();
        appId.setKey(key);
        appId.setNamespace(namespace);
        app.setId(appId);
        app.setStatus("APPROVED");
        appRepository.save(app);
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
        MvcResult mvcResult = mockMvc.perform(get("/v1.0/1234/survey"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists()).andReturn();

        final String contentAsString = mvcResult.getResponse().getContentAsString();

        final SurveyList surveyList = fromJsonString(contentAsString, SurveyList.class);
        assertFalse(surveyList.getData().isEmpty());

        final Survey survey = surveyList.getData().get(surveyList.getData().size()-1);

        assertEquals("Symptoms Checker Survey", survey.getName());
        assertThat(survey.getId()).isNotNull();
        assertEquals(surveyId, survey.getId());
        assertEquals("Symptoms Checker Survey", survey.getDescription());
        assertFalse(survey.getQuestions().isEmpty());

        final Question question = survey.getQuestions().get(0);

        assertQuestionValues(survey, question);
    }

    @Test
    void testGetSurvey() throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(get("/v1.0/1234/survey/1"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String contentAsString = mvcResult.getResponse().getContentAsString();

        final Survey survey = fromJsonString(contentAsString, Survey.class);

        assertEquals("Symptoms Checker Survey", survey.getName());
        assertThat(survey.getId()).isNotNull();

        assertEquals("Symptoms Checker Survey", survey.getDescription());
        assertFalse(survey.getQuestions().isEmpty());

        final Question question = survey.getQuestions().get(0);

        assertQuestionValues(survey, question);
    }

    @Test
    void testGetSurveyNotFound() throws Exception
    {
        mockMvc.perform(get("/v1.0/1234/survey/100000"))
                //.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetSurveysFromInvalidNamespace() throws Exception
    {

        com.privatekit.server.entity.Survey s = new com.privatekit.server.entity.Survey();
        s.setAppKey("s");
        s.setName("3");
        s.setDescription("dd");
        s.setAppNamespace("1234");

        surveyRepository.save(s);

        mockMvc.perform(get("/v1.0/0/survey"))
                //.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostSurvey() throws Exception
    {

        assertTrue(surveyRepository.findByAppNamespace("4567").isEmpty());

        mockMvc.perform(post("/v1.0/4567/survey").content(asJsonString(createMockSurvey()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk());

        final String contentAsString = mockMvc.perform(get("/v1.0/4567/survey")).andReturn().getResponse().getContentAsString();

        final SurveyList list = fromJsonString(contentAsString, SurveyList.class);

        assertFalse(list.getData().isEmpty());

        assertEquals(1, list.getData().size());
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

        final com.privatekit.server.entity.Survey survey = surveyRepository.findByAppNamespace("1234").iterator().next();
        final SurveyResponse response = SurveyResponse.create(1234, list("Great"), true);

        mockMvc.perform(post(String.format("/v1.0/%s/survey/%d/response","1234", survey.getId()))
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
        survey.setName("Symptoms Checker Survey 2");
        survey.setDescription("Symptoms Checker Survey 2");
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

        survey.getScreenTypes().add("Checkbox");
        survey.getScreenTypes().add("911");

        return  survey;
    }

    private void assertQuestionValues(Survey survey, Question question) {
        assertFalse(question.getConditions().isEmpty());
        assertFalse(question.getQuestionDescription().isEmpty());
        assertFalse(question.getQuestionTitle().isEmpty());
        assertFalse(question.getQuestionText().isEmpty());

        assertFalse(survey.getOptions().isEmpty());

        assertFalse(survey.getScreenTypes().isEmpty());

        assertEquals(Lists.list("911", "Checkboox"), survey.getScreenTypes());
    }
}
