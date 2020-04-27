package com.privatekit.server.controller;

import com.privatekit.server.controller.model.Question;
import com.privatekit.server.controller.model.Survey;
import com.privatekit.server.controller.model.SurveyList;
import com.privatekit.server.controller.model.SurveyResponse;
import com.privatekit.server.entity.*;
import com.privatekit.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private SurveyOptionGroupRepository surveyOptionGroupRepository;

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
            questions.forEach(q-> {
                final Question question = Question.from(q);
                survey.getQuestions().add(question);

                final Collection<QuestionCondition> list = questionConditionRepository.findById_SurveyIdAndId_QuestionKey(q.getSurveyId(), q.getOptionKey());

                question.getConditions()
                        .addAll(list.stream()
                                .map(i-> com.privatekit.server.controller.model.QuestionCondition.create(i.getId().getResponse(), i.getJumpToKey()))
                                .collect(Collectors.toList()));

            });

            //collect options

            //collect screenTypes

            surveysList.addSurvey(survey);
        });

        return surveysList;
    }

    @PostMapping(value = "/v1.0/{app_namespace}/survey",
            consumes={MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<String> postSurvey(@PathVariable("app_namespace") String appNamespace,
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

//        // save options
//        survey.getOptions().forEach(o-> {
//
//            final SurveyOptionGroup surveyOptionGroup = new SurveyOptionGroup();
//            surveyOptionGroup.setName(o.getKey());
//
//            final Integer groupId = surveyOptionGroupRepository.save(surveyOptionGroup).getId();
//
//            final com.privatekit.server.entity.SurveyOption surveyOption = com.privatekit.server.entity.SurveyOption.from(o);
//            surveyOption.setOptionGroupId(groupId);
//            surveyOption.getId().setSurveyId(surveyId);
//
//            optionRepository.save(surveyOption);
//        });


        // save screenTypes

        return ResponseEntity.ok("survey was created successfully");

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
