package com.privatekit.server;

import com.privatekit.server.entity.Survey;
import com.privatekit.server.entity.SurveyResponse;
import com.privatekit.server.entity.SurveyResponseId;
import com.privatekit.server.entity.SurveyResponseItem;
import com.privatekit.server.repository.ResponseRepository;
import com.privatekit.server.repository.SurveyRepository;
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

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseRepository responseRepository;

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
        assertThatThrownBy(() -> {
            surveyRepository.save(surveyToTest);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testBasicSurveyResponsePersistenceShouldWork() {
        final Survey persisted = surveyRepository.save(surveyToTest);
        final Set<SurveyResponseItem> items = new HashSet<SurveyResponseItem>();
        final SurveyResponseId id = new SurveyResponseId();
        id.setSurveyId(persisted.getId());
        id.setQuestionKey(QUESTION_KEY_TEST_EXAMPLE_1);
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
        responseRepository.save(response);
        final SurveyResponse found = responseRepository.findById(id).get();
        assertThat(found).isEqualTo(response);
        assertThat(found.getItems().size()).isEqualTo(2);
    }




}
