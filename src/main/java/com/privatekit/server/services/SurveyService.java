package com.privatekit.server.services;

import com.privatekit.server.controller.model.Option;
import com.privatekit.server.controller.model.OptionValue;
import com.privatekit.server.controller.model.Question;
import com.privatekit.server.controller.model.SurveyList;
import com.privatekit.server.entity.*;
import com.privatekit.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Component
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionConditionRepository questionConditionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ScreenTypeRepository screenTypeRepository;

    @Autowired
    private SurveyScreenTypeRepository surveyScreenTypeRepository;

    @Autowired
    private ResponseRepository responseRepository;

    public SurveyList getSurveys(String appNamespace) {
        final SurveyList surveysList = new SurveyList();

        final Collection<Survey> all = surveyRepository.findByAppNamespace(appNamespace);

        all.forEach(s -> {
            final com.privatekit.server.controller.model.Survey survey = com.privatekit.server.controller.model.Survey.from(s);

            final Collection<com.privatekit.server.entity.Question> questions = questionRepository.findBySurveyId(s.getId());

            //collect questions
            questions.forEach(q -> {
                final Question question = Question.from(q);
                survey.getQuestions().add(question);

                final Collection<QuestionCondition> list = questionConditionRepository.findById_SurveyIdAndId_QuestionKey(q.getSurveyId(), q.getId().getQuestionKey());

                question.getConditions()
                        .addAll(list.stream()
                                .map(i -> com.privatekit.server.controller.model.QuestionCondition.create(i.getId().getResponse(), i.getJumpToKey()))
                                .collect(Collectors.toList()));

            });

            //collect options
            final Optional<SurveyOption> surveyOption = optionRepository.findById_SurveyId(s.getId());
            if (surveyOption.isPresent()) {
                final SurveyOption so = surveyOption.get();
                final Set<SurveyOptionValue> values = so.getValues();
                final Option option = new Option();
                option.setKey(so.getId().getOptionKey());

                values.forEach(v -> option.getValues().add(OptionValue.from(v)));

                survey.getOptions().add(option);
            }

            //collect screenTypes
            final Collection<SurveyScreenType> screenTypes = surveyScreenTypeRepository.findById_SurveyId(s.getId());
            screenTypes.forEach(st -> survey.getScreenTypes().add(st.getId().getScreenTypeKey()));

            surveysList.addSurvey(survey);
        });

        return surveysList;
    }

    public void create(App app, com.privatekit.server.controller.model.Survey survey) {

        final com.privatekit.server.entity.Survey surveyDb = com.privatekit.server.entity.Survey.from(survey);
        surveyDb.setAppNamespace(app.getId().getNamespace());
        surveyDb.setAppKey(app.getId().getKey());
        final Integer surveyId = surveyRepository.save(surveyDb).getId();

        // save options
        survey.getOptions().forEach(o -> {

            final com.privatekit.server.entity.SurveyOption surveyOption = com.privatekit.server.entity.SurveyOption.from(o);

            surveyOption.getId().setSurveyId(surveyId);

            optionRepository.save(surveyOption);
        });

        // save screenTypes

        survey.getScreenTypes().forEach(st -> {
            final ScreenType screenType = new ScreenType();
            screenType.setId(st);

            final SurveyScreenTypeId screenTypeId = new SurveyScreenTypeId();
            screenTypeId.setSurveyId(surveyId);
            screenTypeId.setScreenTypeKey(st);

            final SurveyScreenType surveyScreenType = new SurveyScreenType();
            surveyScreenType.setId(screenTypeId);

            screenTypeRepository.save(screenType);
            surveyScreenTypeRepository.save(surveyScreenType);
        });

        // save question
        survey.getQuestions().forEach(q -> {

            final com.privatekit.server.entity.Question questionDb = com.privatekit.server.entity.Question.from(q);

            questionDb.getId().setSurveyId(surveyId);

            questionRepository.save(questionDb);

        });

        // save question conditions
        survey.getQuestions().forEach(q -> {
            q.getConditions().forEach(qc -> {
                final com.privatekit.server.entity.QuestionCondition questionConditionDb = com.privatekit.server.entity.QuestionCondition.from(qc);
                questionConditionDb.getId().setSurveyId(surveyId);
                questionConditionDb.getId().setQuestionKey(q.getQuestionKey());
                questionConditionDb.getId().setResponse(qc.getResponse());
                questionConditionRepository.save(questionConditionDb);
            });
        });
    }

    public void createSurveyResponses(Integer surveyId, List<com.privatekit.server.controller.model.SurveyResponse> surveyResponses) {
        surveyResponses.forEach(i -> {
            final com.privatekit.server.entity.SurveyResponse sr = new com.privatekit.server.entity.SurveyResponse();

            final List<String> values = i.getResponseValue();

            final SurveyResponseId id = new SurveyResponseId();
            id.setSurveyId(surveyId);

            id.setQuestionKey(Integer.toString(i.getQuestionId()));
            sr.setId(id);
            sr.setSkipped(i.isSkipped());

            sr.setItems(values.stream().map(v -> {
                final SurveyResponseItem surveyResponseItem = new SurveyResponseItem();
                surveyResponseItem.setValue(v);
                surveyResponseItem.setResponse(sr);
                return surveyResponseItem;
            }).collect(toSet()));

            responseRepository.save(sr);
        });
    }
}
