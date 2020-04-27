package com.privatekit.services.repository;

import com.privatekit.services.entity.QuestionOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository extends CrudRepository<QuestionOption, Integer> {
}
