package com.privatekit.server.repository;

import com.privatekit.server.entity.SurveyScreenType;
import com.privatekit.server.entity.SurveyScreenTypeId;
import org.springframework.data.repository.CrudRepository;

public interface SurveyScreenTypeRepository extends CrudRepository<SurveyScreenType, SurveyScreenTypeId> {
}
