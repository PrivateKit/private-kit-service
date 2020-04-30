package com.privatekit.server.controller.model;

import org.junit.jupiter.api.Test;

import static com.google.inject.internal.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testOptions() {
        final OptionValue yes = OptionValue.create("Yes", "Y", "Yes option");
        final OptionValue no = OptionValue.create("No", "N");
        final Option option = new Option();

        option.setKey("key");
        option.setValues(newArrayList(yes, no));

        assertEquals("key", option.getKey());
        assertEquals(2, option.getValues().size());

        option.setKey("new");
        assertEquals("new", option.getKey());

        assertEquals("Yes option", yes.getDescription());
        assertNotNull(no.getDescription());
        assertEquals("", no.getDescription());
    }

    @Test
    public void testQuestion() {
        final Question q1 = new Question();
        q1.setQuestionKey("1");
        q1.setQuestionText("Select your symptoms?");
        q1.setQuestionType("MULTI");
        q1.setRequired(true);
        q1.setScreenType("Checkbox");
        q1.setOptionKey("option_1");

        assertEquals("1", q1.getQuestionKey());
        assertEquals("Select your symptoms?", q1.getQuestionText());
        assertEquals("MULTI", q1.getQuestionType());
        assertTrue(q1.isRequired());
        assertEquals("Checkbox", q1.getScreenType());
        assertEquals("option_1", q1.getOptionKey());
    }

    @Test
    public void testSurveyList() {
        final SurveyList surveyList = new SurveyList();
        assertTrue(surveyList.getData().isEmpty());

        surveyList.addSurvey(new Survey());

        assertFalse(surveyList.getData().isEmpty());
    }

    @Test
    public void testApp() {
        final App app = App.create("n", "k", "PENDING");

        assertEquals("n", app.getNamespace());
        assertEquals("k", app.getKey());
        assertEquals("PENDING", app.getStatus());

        final App emptyApp = new App();

        assertNull(emptyApp.getNamespace());
        assertNull(emptyApp.getKey());
        assertNull(emptyApp.getStatus());

        emptyApp.setKey("k1");
        emptyApp.setNamespace("n1");
        emptyApp.setStatus("REJECTED");

        assertEquals("n1", emptyApp.getNamespace());
        assertEquals("k1", emptyApp.getKey());
        assertEquals("REJECTED", emptyApp.getStatus());
    }
}
