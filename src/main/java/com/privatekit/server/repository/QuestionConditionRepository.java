package com.privatekit.server.repository;

import com.privatekit.server.entity.QuestionCondition;
import com.privatekit.server.entity.QuestionConditionId;
import org.springframework.data.repository.CrudRepository;

public interface QuestionConditionRepository extends CrudRepository<QuestionCondition, QuestionConditionId> {
}
