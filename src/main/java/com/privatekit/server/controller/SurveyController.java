package com.privatekit.server.controller;

import com.privatekit.server.controller.model.Survey;
import com.privatekit.server.controller.model.SurveyList;
import com.privatekit.server.controller.model.SurveyResponse;
import com.privatekit.server.entity.App;
import com.privatekit.server.repository.AppRepository;
import com.privatekit.server.repository.SurveyRepository;
import com.privatekit.server.services.SurveyService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class SurveyController {

    private final AppRepository appRepository;

    private final SurveyRepository surveyRepository;

    private final SurveyService surveyService;

    public SurveyController(AppRepository appRepository, SurveyRepository surveyRepository, SurveyService surveyService) {
        this.appRepository = appRepository;
        this.surveyRepository = surveyRepository;
        this.surveyService = surveyService;
    }

    @ExceptionHandler({ PersistenceException.class})
    public ResponseEntity<Object> handleException() {
        return new ResponseEntity<>(
                "An error occurred processing the request.", new HttpHeaders(), BAD_REQUEST);
    }

    @GetMapping(value = "/v1.0/{app_namespace}/survey")
    public @ResponseBody
    SurveyList getSurveys(@PathVariable("app_namespace") String appNamespace) {

        resolveApplicationApproved(appNamespace);

        return surveyService.getSurveys(appNamespace);
    }

    @GetMapping(value = "/v1.0/{app_namespace}/survey/{survey_id}")
    public @ResponseBody
    Survey getSurveys(@PathVariable("app_namespace") String appNamespace, @PathVariable("survey_id") Integer surveryId) {

        resolveApplicationApproved(appNamespace);

        final Optional<Survey> survey = surveyService.getSurvey(surveryId);
        if (survey.isEmpty())
            throw new ResponseStatusException(NOT_FOUND, "Survey not Found");
        return survey.get();
    }

    @PostMapping(value = "/v1.0/{app_namespace}/survey",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    public ResponseEntity<String> postSurvey(@PathVariable("app_namespace") String appNamespace,
                                             @Validated @RequestBody Survey survey) {

        final App app = resolveApplicationApproved(appNamespace);

        surveyService.create(app, survey);

        return ResponseEntity.ok("survey was created successfully");

    }

    @PostMapping(value = "/v1.0/{app_namespace}/survey/{survey_id}/response",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    public void addSurveyResponse(@PathVariable("app_namespace") String appNamespace,
                                  @PathVariable("survey_id") int surveyId,
                                  @Validated @RequestBody List<SurveyResponse> surveyResponses) {

        final Optional<com.privatekit.server.entity.Survey> survey = surveyRepository.findByAppNamespaceAndId(appNamespace, surveyId);

        if (survey.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Survey not found");
        }

        surveyService.createSurveyResponses(survey.get().getId(), surveyResponses);

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


    private App resolveApplicationApproved(String appNamespace) {
        final Optional<App> app = appRepository.findById_NamespaceAndStatus(appNamespace, "APPROVED");

        if (app.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "App not approved");
        }
        return app.get();
    }
}
