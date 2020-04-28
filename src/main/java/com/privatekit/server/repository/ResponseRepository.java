package com.privatekit.server.repository;

import com.privatekit.server.entity.SurveyResponse;
import com.privatekit.server.entity.SurveyResponseId;
import org.springframework.data.repository.CrudRepository;

public interface ResponseRepository extends CrudRepository<SurveyResponse, SurveyResponseId> {
}
