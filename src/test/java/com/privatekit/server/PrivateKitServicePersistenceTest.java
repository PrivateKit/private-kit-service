package com.privatekit.server;

import com.privatekit.server.entity.*;
import com.privatekit.server.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({PrivateKitService.class})
public class PrivateKitServicePersistenceTest {

    private static final String SURVEY_TEST_EXAMPLE_NAME = "COVID Survey";

    private static final String SURVEY_TEST_EXAMPLE_DESC = "This is a test Survey";

    private static final String SURVEY_TEST_EXAMPLE_IMG = "";

    private static final String SURVEY_TEST_EXAMPLE_APP_NAMESPACE = "APP-NS";

    private static final String SURVEY_TEST_EXAMPLE_APP_KEY = "APP1";

    private static final String RESPONSE_TEST_EXAMPLE_1 = "true";

    private static final String RESPONSE_TEST_EXAMPLE_2 = "false";

    private static final Integer QUESTION_KEY_TEST_EXAMPLE_1 = 1;

    private static final String QUESTION_TYPE_EXAMPLE1 = "MULTI";

    private static final String QUESTION_SCREEN_TYPE_EXAMPLE1 = "Checkbox";

    private static final String QUESTION_OPTION_KEY_EXAMPLE1 = "option_1";

    private static final String QUESTION_CONDITION_RESPONSE_EXAMPLE1 = "Y";

    private static final Integer QUESTION_CONDITION_JUMP_TO_EXAMPLE1 = 2;

    private static final String OPTION_KEY_EXAMPLE1 = "option_1";

    private static final String OPTION_LABEL_EXAMPLE1 = "Yes";

    private static final String OPTION_VALUE_EXAMPLE1 = "Y";

    private static final String OPTION_DESC_EXAMPLE1 = "";

    private static final String OPTION_LABEL_EXAMPLE2 = "No";

    private static final String OPTION_VALUE_EXAMPLE2 = "N";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionConditionRepository questionConditionRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Survey surveyToTest;

    @BeforeEach
    public void setup() {
        surveyToTest = new Survey();
        surveyToTest.setName(SURVEY_TEST_EXAMPLE_NAME);
        surveyToTest.setDescription(SURVEY_TEST_EXAMPLE_DESC);
        surveyToTest.setImage(SURVEY_TEST_EXAMPLE_IMG);
        surveyToTest.setAppNamespace(SURVEY_TEST_EXAMPLE_APP_NAMESPACE);
        surveyToTest.setAppKey(SURVEY_TEST_EXAMPLE_APP_KEY);
    }

    @Test
    public void testBasicSurveyPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        assertThat(persisted.getId()).isNotNull();
    }

    @Test
    public void testBasicSurveyPersistenceWithoutNullableShouldWork() {
        surveyToTest.setImage(null);
        surveyToTest.setDescription(null);
        final Survey persisted = surveyRepository.save(surveyToTest);
        assertThat(persisted.getId()).isNotNull();
    }

    @Test
    public void testBasicSurveyPersistenceWithoutNotNullShouldNotWork() {
        surveyToTest.setName(null);
        assertThatThrownBy(() -> surveyRepository.save(surveyToTest)).
                isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testBasicResponsePersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final Set<SurveyResponseItem> items = new HashSet<SurveyResponseItem>();
        final SurveyResponseId id = buildResponseId(persisted);
        final SurveyResponse response = buildResponse(items, id);
        responseRepository.save(response);
        final Optional<SurveyResponse> maybeSurvey = responseRepository.findById(id);
        assertThat(maybeSurvey.isPresent()).isTrue();
        assertThat(maybeSurvey.get()).isEqualTo(response);
        assertThat(maybeSurvey.get().getItems().size()).isEqualTo(2);
    }

    private SurveyResponseId buildResponseId(Survey persisted) {
        final SurveyResponseId id = new SurveyResponseId();
        id.setSurveyId(persisted.getId());
        id.setQuestionKey(QUESTION_KEY_TEST_EXAMPLE_1);
        return id;
    }

    private SurveyResponse buildResponse(Set<SurveyResponseItem> items, SurveyResponseId id) {
        final SurveyResponse response = new SurveyResponse();
        final SurveyResponseItem item1 = new SurveyResponseItem();
        item1.setValue(RESPONSE_TEST_EXAMPLE_1);
        item1.setResponse(response);
        items.add(item1);
        final SurveyResponseItem item2 = new SurveyResponseItem();
        item2.setValue(RESPONSE_TEST_EXAMPLE_2);
        item2.setResponse(response);
        items.add(item2);
        response.setItems(items);
        response.setId(id);
        response.setSkipped(false);
        return response;
    }

    @Test
    public void testBasicQuestionPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final Question question = buildQuestion(persisted);
        final Question questionPersisted = questionRepository.save(question);
        assertThat(questionPersisted).isEqualTo(question);
    }

    private Question buildQuestion(Survey persisted) {
        final QuestionId id = new QuestionId();
        id.setQuestionKey(QUESTION_KEY_TEST_EXAMPLE_1);
        id.setSurveyId(persisted.getId());
        final Question question = new Question();
        question.setId(id);
        question.setImage("");
        question.setType(QUESTION_TYPE_EXAMPLE1);
        question.setScreenType(QUESTION_SCREEN_TYPE_EXAMPLE1);
        question.setRequired(true);
        question.setOptionKey(QUESTION_OPTION_KEY_EXAMPLE1);
        return question;
    }

    @Test
    public void testBasicQuestionConditionPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        QuestionCondition condition = buildCondition(persisted);
        final QuestionCondition questionConditionPersisted = questionConditionRepository.save(condition);
        assertThat(questionConditionPersisted).isEqualTo(condition);
    }

    private QuestionCondition buildCondition(Survey persisted) {
        QuestionConditionId id = new QuestionConditionId();
        id.setQuestionKey(QUESTION_KEY_TEST_EXAMPLE_1);
        id.setResponse(QUESTION_CONDITION_RESPONSE_EXAMPLE1);
        id.setSurveyId(persisted.getId());
        QuestionCondition condition = new QuestionCondition();
        condition.setId(id);
        condition.setJumpToKey(QUESTION_CONDITION_JUMP_TO_EXAMPLE1);
        return condition;
    }

    @Test
    public void testBasicOptionsPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final SurveyOption option = buildOption(persisted);
        SurveyOption optionPersisted = optionRepository.save(option);
        assertThat(optionPersisted).isEqualTo(option);
        assertThat(optionPersisted.getValues().size()).isEqualTo(2);
    }

    private SurveyOption buildOption(Survey persisted) {
        final SurveyOptionId id = new SurveyOptionId();
        id.setOptionKey(OPTION_KEY_EXAMPLE1);
        id.setSurveyId(persisted.getId());
        final SurveyOption option = new SurveyOption();
        final Set<SurveyOptionValue> values = new HashSet<SurveyOptionValue>();
        values.add(new SurveyOptionValue(OPTION_LABEL_EXAMPLE1, OPTION_VALUE_EXAMPLE1, OPTION_DESC_EXAMPLE1, option));
        values.add(new SurveyOptionValue(OPTION_LABEL_EXAMPLE2, OPTION_VALUE_EXAMPLE2, null, option));
        option.setId(id);
        option.setValues(values);
        return option;
    }

    @Test
    public void testCompleteSurveyPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final SurveyOption option = buildOption(persisted);
        final Question question = buildQuestion(persisted);
        final QuestionCondition condition = buildCondition(persisted);
        final SurveyOption optionPersisted = optionRepository.save(option);
        final Question questionPersisted = questionRepository.save(question);
        final QuestionCondition conditionPersisted = questionConditionRepository.save(condition);
        assertThat(option).isEqualTo(optionPersisted);
        assertThat(question).isEqualTo(questionPersisted);
        assertThat(condition).isEqualTo(conditionPersisted);
    }


}
