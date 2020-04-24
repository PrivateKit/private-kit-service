package com.privatekit.services.controller;

import com.google.inject.internal.util.Lists;
import com.privatekit.services.controller.model.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SurveyController {

    @GetMapping(value = "/v1.0/{app_id}/survey")
    public @ResponseBody SurveyList getSurveys(@PathVariable("app_id") String appId) {
        final SurveyList surveysList = new SurveyList();

        // ---------
        // Mock Data
        // ---------

        final Survey survey = new Survey();
        survey.setName("Symptoms Checker Survey");

        final Question q1 = new Question();

        q1.setQuestionKey("1");
        q1.setQuestionText("Select your symptoms?");
        q1.setQuestionType("MULTI");
        q1.setRequired(true);
        q1.setScreenType("Checkbox");
        q1.setOptionKey("option_1");

        q1.getConditions().addAll(Lists.newArrayList(QuestionCondition.create("Y", "3"), QuestionCondition.create("N", "2")));


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
        opt1.setValues(Lists.newArrayList(OptionValue.create("Yes", "Y"), OptionValue.create("No", "N"), OptionValue.create("Maybe", "M")));

        survey.getOptions().add(opt1);

        survey.getScreenTypes().put("Checkbox", "911");

        surveysList.addSurvey(survey);

        return surveysList;
    }

    @PostMapping(value = "/v1.0/{app_namespace}/survey",
            consumes={MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void postSurvey(@PathVariable("app_namespace") String appNamespace,
                           @Validated @RequestBody Survey survey) {

    }

    @PostMapping(value = "/v1.0/{app_id}/survey/{survey_id}/response",
            consumes={MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void addSurveyResponse(@PathVariable("app_id") String appId,
                                  @PathVariable("survey_id") String surveyId,
                                  @Validated @RequestBody List<SurveyResponse> surveyResponses) {
    }

//    @PostMapping(value = "/v1.0/app/register",
//            consumes={MediaType.APPLICATION_JSON_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE})
//    public void registerApp() {
//
//    }
//    @PutMapping(value = "/v1.0/app/{namespace}",
//            consumes={MediaType.APPLICATION_JSON_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE} )
//    public void updateApp(@PathVariable("namespace") String namespace, @RequestBody App app) {
//
//    }
//
//    @GetMapping(value = "/v1.0/app/{namespace}",
//            consumes={MediaType.APPLICATION_JSON_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<App> getApp(@PathVariable("namespace") String namespace) {
//
//        final App app = App.create("safepath", "Oj6m04uiCb", "PENDING");
//        return ResponseEntity.ok(app);
//    }
//
//    @PutMapping(value = "/v1.0/{app_namespace}/survey/{survey_id}",
//            consumes={MediaType.APPLICATION_JSON_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE})
//    public void updateSurvey(@PathVariable("app_namespace") String appNamespace, @PathVariable("survey_id") String surveyId, @RequestBody Survey survey) {
//
//    }

}
