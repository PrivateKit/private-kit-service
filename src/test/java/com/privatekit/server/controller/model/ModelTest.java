package com.privatekit.server.controller.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

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
