package com.privatekit.server.repository;

import com.privatekit.server.entity.SurveyScreenType;
import com.privatekit.server.entity.SurveyScreenTypeId;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface SurveyScreenTypeRepository extends CrudRepository<SurveyScreenType, SurveyScreenTypeId> {

    Collection<SurveyScreenType> findById_SurveyId(Integer surveyId);
}
