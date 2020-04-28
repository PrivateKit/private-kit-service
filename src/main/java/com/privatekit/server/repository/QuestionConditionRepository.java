package com.privatekit.server.repository;

import com.privatekit.server.entity.QuestionCondition;
import com.privatekit.server.entity.QuestionConditionId;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface QuestionConditionRepository extends CrudRepository<QuestionCondition, QuestionConditionId> {

    Collection<QuestionCondition> findById_SurveyIdAndId_QuestionKey(Integer surveyId, String questionKey);
}
