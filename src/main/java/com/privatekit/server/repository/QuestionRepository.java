package com.privatekit.server.repository;

import com.privatekit.server.entity.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface QuestionRepository extends CrudRepository<Question, Integer> {

    Collection<Question> findBySurveyId(Integer surveyId);
}
