package com.privatekit.services.controller;

import com.privatekit.services.controller.model.Survey;
import com.privatekit.services.controller.model.SurveyList;
import com.privatekit.services.controller.model.SurveyResponse;
import com.privatekit.services.entity.SurveyResponseId;
import com.privatekit.services.entity.SurveyResponseItem;
import com.privatekit.services.repository.ResponseRepository;
import com.privatekit.services.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class SurveyController {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @GetMapping(value = "/v1.0/{app_namespace}/survey")
    public @ResponseBody SurveyList getSurveys(@PathVariable("app_namespace") String appNamespace) {

        final SurveyList surveysList = new SurveyList();

        final Collection<com.privatekit.services.entity.Survey> all = surveyRepository.findByAppNamespace(appNamespace);

        all.forEach(s -> {
            final Survey from = Survey.from(s);

            //collect questions
            //collect options
            //collect screenTypes

            surveysList.addSurvey(from);
        });

        return surveysList;
//        // ---------
//        // Mock Data
//        // ---------
//
//        final Survey survey = new Survey();
//        survey.setName("Symptoms Checker Survey");
//
//        final Question q1 = new Question();
//
//        q1.setQuestionKey("1");
//        q1.setQuestionText("Select your symptoms?");
//        q1.setQuestionType("MULTI");
//        q1.setRequired(true);
//        q1.setScreenType("Checkbox");
//        q1.setOptionKey("option_1");
//
//        q1.getConditions().addAll(Lists.newArrayList(QuestionCondition.create("Y", "3"), QuestionCondition.create("N", "2")));
//
//
//        final Question q2 = new Question();
//        q2.setQuestionKey("2");
//        q2.setQuestionType("END");
//
//        final Question q3 = new Question();
//        q3.setQuestionKey("3");
//        q3.setQuestionType("END");
//
//        survey.getQuestions().add(q1);
//        survey.getQuestions().add(q2);
//        survey.getQuestions().add(q3);
//
//        final Option opt1 = new Option();
//        opt1.setKey("option_1");
//        opt1.setValues(Lists.newArrayList(OptionValue.create("Yes", "Y"), OptionValue.create("No", "N"), OptionValue.create("Maybe", "M")));
//
//        survey.getOptions().add(opt1);
//
//        survey.getScreenTypes().put("Checkbox", "911");
//
//        surveysList.addSurvey(survey);
    }

    @PostMapping(value = "/v1.0/{app_namespace}/survey",
            consumes={MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    public void postSurvey(@PathVariable("app_namespace") String appNamespace,
                           @Validated @RequestBody Survey survey) {

        final com.privatekit.services.entity.Survey surveyDb = com.privatekit.services.entity.Survey.from(survey);
        surveyDb.setAppNamespace(appNamespace);
        surveyRepository.save(surveyDb);

        // save question
        // save options
        // save screenTypes

    }

    @PostMapping(value = "/v1.0/{app_namespace}/survey/{survey_id}/response",
            consumes={MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    public void addSurveyResponse(@PathVariable("app_namespace") String appNamespace,
                                  @PathVariable("survey_id") int surveyId,
                                  @Validated @RequestBody List<SurveyResponse> surveyResponses) {

        final Optional<com.privatekit.services.entity.Survey> survey = surveyRepository.find(appNamespace, surveyId);

        if (survey.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Survey not found");
        }

        surveyResponses.forEach(i ->{
            final com.privatekit.services.entity.SurveyResponse sr = new com.privatekit.services.entity.SurveyResponse();

            final List<String> values = i.getResponseValue();

            final SurveyResponseId id = new SurveyResponseId();
            id.setSurveyId(surveyId);
            id.setQuestionKey(i.getQuestionId());
            sr.setId(id);
            sr.setSkipped(i.isSkkiped());

            sr.setItems(values.stream().map(v-> {
                final SurveyResponseItem surveyResponseItem = new SurveyResponseItem();
                surveyResponseItem.setValue(v);
                surveyResponseItem.setResponse(sr);
                return surveyResponseItem;
            }).collect(toSet()));

            responseRepository.save(sr);
        });
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
