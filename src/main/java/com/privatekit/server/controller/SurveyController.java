package com.privatekit.server.controller;

import com.privatekit.server.controller.model.Question;
import com.privatekit.server.controller.model.Survey;
import com.privatekit.server.controller.model.SurveyList;
import com.privatekit.server.controller.model.SurveyResponse;
import com.privatekit.server.entity.QuestionId;
import com.privatekit.server.entity.SurveyResponseId;
import com.privatekit.server.entity.SurveyResponseItem;
import com.privatekit.server.repository.*;
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
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionConditionRepository questionConditionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @GetMapping(value = "/v1.0/{app_namespace}/survey")
    @Transactional
    public @ResponseBody SurveyList getSurveys(@PathVariable("app_namespace") String appNamespace) {

        final SurveyList surveysList = new SurveyList();

        final Collection<com.privatekit.server.entity.Survey> all = surveyRepository.findByAppNamespace(appNamespace);

        all.forEach(s -> {
            final Survey survey = Survey.from(s);

            final Collection<com.privatekit.server.entity.Question> questions = questionRepository.findBySurveyId(s.getId());

            //collect questions
            questions.forEach(q-> survey.getQuestions().add(Question.from(q)));

            //optionRepository.findById(s.getId())

            //collect options
            //collect screenTypes

            surveysList.addSurvey(survey);
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

        final com.privatekit.server.entity.Survey surveyDb = com.privatekit.server.entity.Survey.from(survey);
        surveyDb.setAppNamespace(appNamespace);

        final Integer surveyId = surveyRepository.save(surveyDb).getId();

        // save question
        survey.getQuestions().forEach(q->{

            final com.privatekit.server.entity.Question questionDb = com.privatekit.server.entity.Question.from(q);

            questionDb.getId().setSurveyId(surveyId);

            final QuestionId questionId = questionRepository.save(questionDb).getId();

            q.getConditions().forEach(qc ->{

                final com.privatekit.server.entity.QuestionCondition questionConditionDb =  com.privatekit.server.entity.QuestionCondition.from(qc);
                questionConditionDb.getId().setSurveyId(questionId.getSurveyId());
                questionConditionDb.getId().setQuestionKey(questionId.getQuestionKey());
                questionConditionDb.getId().setResponse(qc.getResponse());
                questionConditionRepository.save(questionConditionDb);

            });
        });


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

        final Optional<com.privatekit.server.entity.Survey> survey = surveyRepository.findByAppNamespaceAndId(appNamespace, surveyId);

        if (survey.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Survey not found");
        }

        surveyResponses.forEach(i ->{
            final com.privatekit.server.entity.SurveyResponse sr = new com.privatekit.server.entity.SurveyResponse();

            final List<String> values = i.getResponseValue();

            final SurveyResponseId id = new SurveyResponseId();
            id.setSurveyId(surveyId);

            // ------------------------
            // TODO !!!!HACK until the API model get consistency between data types
            // ------------------------
            id.setQuestionKey(Integer.toString(i.getQuestionId()));
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
