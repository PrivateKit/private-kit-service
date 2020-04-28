package com.privatekit.server;

import com.privatekit.server.entity.*;
import com.privatekit.server.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private static final String QUESTION_KEY_TEST_EXAMPLE_1 = "1";

    private static final String QUESTION_TYPE_EXAMPLE1 = "MULTI";

    private static final String QUESTION_SCREEN_TYPE_EXAMPLE1 = "Checkbox";

    private static final String QUESTION_SCREEN_TYPE_EXAMPLE2 = "911";

    private static final String QUESTION_OPTION_KEY_EXAMPLE1 = "option_1";

    private static final String QUESTION_CONDITION_RESPONSE_EXAMPLE1 = "Y";

    private static final String QUESTION_CONDITION_JUMP_TO_EXAMPLE1 = "2";

    private static final String OPTION_KEY_EXAMPLE1 = "option_1";

    private static final String OPTION_LABEL_EXAMPLE1 = "Yes";

    private static final String OPTION_VALUE_EXAMPLE1 = "Y";

    private static final String OPTION_DESC_EXAMPLE1 = "";

    private static final String OPTION_LABEL_EXAMPLE2 = "No";

    private static final String OPTION_VALUE_EXAMPLE2 = "N";

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

    @Autowired
    private ScreenTypeRepository screenTypeRepository;

    @Autowired
    private SurveyScreenTypeRepository surveyScreenTypeRepository;

    @Autowired
    private AppRepository appRepository;

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
    public void testCreateApp() {
        final App app = new App();
        final AppId id = new AppId();
        id.setNamespace("A");
        id.setKey("B");
        app.setId(id);
        app.setStatus("PENDING");

        final App save = appRepository.save(app);

        assertThat(save.getStatus()).isEqualTo("PENDING");
        assertThat(save).isEqualTo(app);
        assertThat(save.getId()).isEqualTo(app.getId());
        assertThat(save.getId()).isNotEqualTo(new AppId());
        assertThat(save.hashCode()).isEqualTo(app.hashCode());
    }

    @Test
    public void testBasicSurveyPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);

        assertThat(persisted).isEqualTo(surveyToTest);
        assertThat(persisted.getId()).isEqualTo(surveyToTest.getId());
        assertThat(persisted.hashCode()).isEqualTo(surveyToTest.hashCode());

        assertThat(persisted.getId()).isNotNull();
        assertThat(persisted.getAppNamespace()).isNotNull();
        assertThat(persisted.getAppNamespace()).isEqualTo(SURVEY_TEST_EXAMPLE_APP_NAMESPACE);
        assertThat(persisted.getAppKey()).isNotNull();
        assertThat(persisted.getAppKey()).isEqualTo(SURVEY_TEST_EXAMPLE_APP_KEY);
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
    public void testSurveyComparisionShouldNotBeEquals() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final Survey emptySurvey = new Survey();
        assertThat(persisted).isNotEqualTo(emptySurvey);
    }

    @Test
    public void testSurveyComparisionToNullShouldNotBeEquals() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final Survey emptySurvey = null;
        assertThat(persisted).isNotEqualTo(emptySurvey);
    }

    @Test
    public void testBasicResponsePersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final Set<SurveyResponseItem> items = new HashSet<>();
        final SurveyResponseId id = buildResponseId(persisted);
        final SurveyResponse response = buildResponse(items, id);

        responseRepository.save(response);

        final Optional<SurveyResponse> maybeSurvey = responseRepository.findById(id);

        assertThat(maybeSurvey.isPresent()).isTrue();
        assertTrue(maybeSurvey.isPresent());

        final SurveyResponse surveyResponse = maybeSurvey.get();

        assertThat(surveyResponse.getSkipped()).isFalse();
        assertThat(surveyResponse).isEqualTo(response);
        assertThat(surveyResponse.getId()).isEqualTo(response.getId());
        assertThat(surveyResponse.getId()).isNotEqualTo(new SurveyResponseId());
        assertThat(surveyResponse.getItems().size()).isEqualTo(2);
    }

    @Test
    public void testBasicQuestionPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final Question question = buildQuestion(persisted);
        final Question questionPersisted = questionRepository.save(question);

        assertThat(questionPersisted).isEqualTo(question);
        assertThat(questionPersisted.getId()).isEqualTo(question.getId());
        assertThat(questionPersisted.getId()).isNotEqualTo(new QuestionId());
        assertThat(questionPersisted.hashCode()).isEqualTo(question.hashCode());
    }

    @Test
    public void testBasicQuestionConditionPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final QuestionCondition condition = buildCondition(persisted);

        final QuestionCondition questionConditionPersisted = questionConditionRepository.save(condition);

        assertThat(questionConditionPersisted.getId().getSurveyId()).isEqualTo(condition.getId().getSurveyId());
        assertThat(questionConditionPersisted.getId().getQuestionKey()).isEqualTo(condition.getId().getQuestionKey());
        assertThat(questionConditionPersisted).isEqualTo(condition);
        assertThat(questionConditionPersisted.getId()).isEqualTo(condition.getId());
        assertThat(questionConditionPersisted.getId()).isNotEqualTo(new QuestionConditionId());
        assertThat(questionConditionPersisted.hashCode()).isEqualTo(condition.hashCode());
    }

    @Test
    public void testBasicOptionsPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final SurveyOption option = buildOption(persisted);

        final SurveyOption optionPersisted = optionRepository.save(option);

        assertThat(optionPersisted).isEqualTo(option);
        assertThat(optionPersisted.getId()).isEqualTo(option.getId());
        assertThat(optionPersisted.getId()).isNotEqualTo(new SurveyOptionId());
        assertThat(optionPersisted.getValues().size()).isEqualTo(2);

        optionPersisted.getValues().forEach(v->{
            assertThat(v.getId()).isNotNull();
            assertThat(v.getOption()).isNotNull();
            assertThat(v.getOptionLabel()).isNotNull();
            assertThat(v.getOptionValue()).isNotNull();
        });
    }

    @Test
    public void testBasicScreenTypePersistenceShouldWork() {
        final ScreenType screenType1 = new ScreenType();
        screenType1.setId(QUESTION_SCREEN_TYPE_EXAMPLE1);
        screenType1.setDescription("Hello World");

        final ScreenType screenType2 = new ScreenType();
        screenType2.setId(QUESTION_SCREEN_TYPE_EXAMPLE2);

        final ScreenType screenTypePersisted1 = screenTypeRepository.save(screenType1);
        final ScreenType screenTypePersisted2 =screenTypeRepository.save(screenType2);

        assertThat(screenTypePersisted1).isEqualTo(screenType1);
        assertThat(screenTypePersisted1.hashCode()).isEqualTo(screenType1.hashCode());
        assertThat(screenTypePersisted1.getId()).isEqualTo(screenType1.getId());
        assertThat(screenTypePersisted1.getId()).isNotEqualTo(new SurveyScreenTypeId());
        assertThat(screenTypePersisted1.hashCode()).isEqualTo(screenType1.hashCode());
        assertThat(screenTypePersisted1.getDescription()).isEqualTo("Hello World");
        assertThat(screenTypePersisted2).isEqualTo(screenType2);
        assertThat(screenTypePersisted2.getDescription()).isNull();
    }

    @Test
    public void testCompleteSurveyPersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final SurveyOption option = buildOption(persisted);
        final Question question = buildQuestion(persisted);
        final QuestionCondition condition = buildCondition(persisted);
        final SurveyOption optionPersisted = optionRepository.save(option);
        final ScreenType screenType1 = new ScreenType();
        screenType1.setId(QUESTION_SCREEN_TYPE_EXAMPLE1);

        final ScreenType screenType2 = new ScreenType();
        screenType2.setId(QUESTION_SCREEN_TYPE_EXAMPLE2);

        final SurveyScreenTypeId surveyScreenTypeId1 = new SurveyScreenTypeId();
        surveyScreenTypeId1.setSurveyId(persisted.getId());
        surveyScreenTypeId1.setScreenTypeKey(QUESTION_SCREEN_TYPE_EXAMPLE1);

        final SurveyScreenType surveyScreenType1 = new SurveyScreenType();
        surveyScreenType1.setId(surveyScreenTypeId1);

        final SurveyScreenTypeId surveyScreenTypeId2 = new SurveyScreenTypeId();
        surveyScreenTypeId2.setSurveyId(persisted.getId());
        surveyScreenTypeId2.setScreenTypeKey(QUESTION_SCREEN_TYPE_EXAMPLE1);

        final SurveyScreenType surveyScreenType2 = new SurveyScreenType();
        surveyScreenType2.setId(surveyScreenTypeId2);

        final ScreenType screenTypePersisted1 = screenTypeRepository.save(screenType1);
        final ScreenType screenTypePersisted2 =screenTypeRepository.save(screenType2);
        final SurveyScreenType surveyScreenTypePersisted1 = surveyScreenTypeRepository.save(surveyScreenType1);
        final SurveyScreenType surveyScreenTypePersisted2 = surveyScreenTypeRepository.save(surveyScreenType2);
        final Question questionPersisted = questionRepository.save(question);
        final QuestionCondition conditionPersisted = questionConditionRepository.save(condition);

        assertThat(option).isEqualTo(optionPersisted);
        assertThat(question).isEqualTo(questionPersisted);
        assertThat(condition).isEqualTo(conditionPersisted);

        assertThat(screenTypePersisted1).isEqualTo(screenType1);
        assertThat(screenTypePersisted2).isEqualTo(screenType2);

        assertThat(surveyScreenTypePersisted1).isEqualTo(surveyScreenType1);
        assertThat(surveyScreenTypePersisted1.hashCode()).isEqualTo(surveyScreenType1.hashCode());
        assertThat(surveyScreenTypePersisted1.getId()).isEqualTo(surveyScreenType1.getId());
        assertThat(surveyScreenTypePersisted2).isEqualTo(surveyScreenType2);
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


    private SurveyOption buildOption(Survey persisted) {
        final SurveyOptionId id = new SurveyOptionId();
        id.setOptionKey(OPTION_KEY_EXAMPLE1);
        id.setSurveyId(persisted.getId());

        final SurveyOption option = new SurveyOption();
        final Set<SurveyOptionValue> values = new HashSet<>();
        values.add(new SurveyOptionValue(OPTION_LABEL_EXAMPLE1, OPTION_VALUE_EXAMPLE1, OPTION_DESC_EXAMPLE1, option));
        values.add(new SurveyOptionValue(OPTION_LABEL_EXAMPLE2, OPTION_VALUE_EXAMPLE2, null, option));
        option.setId(id);
        option.setValues(values);

        return option;
    }

    private QuestionCondition buildCondition(Survey persisted) {
        final QuestionConditionId id = new QuestionConditionId();
        id.setQuestionKey(QUESTION_KEY_TEST_EXAMPLE_1);
        id.setResponse(QUESTION_CONDITION_RESPONSE_EXAMPLE1);
        id.setSurveyId(persisted.getId());

        final QuestionCondition condition = new QuestionCondition();
        condition.setId(id);
        condition.setJumpToKey(QUESTION_CONDITION_JUMP_TO_EXAMPLE1);
        return condition;
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

}
