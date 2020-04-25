package com.privatekit.services.repository;

import com.privatekit.services.entity.SurveyResponse;
import com.privatekit.services.entity.SurveyResponseId;
import org.springframework.data.repository.CrudRepository;

public interface ResponseRepository extends CrudRepository<SurveyResponse, SurveyResponseId> {
}
