package com.privatekit.server.repository;

import com.privatekit.server.entity.SurveyOption;
import com.privatekit.server.entity.SurveyOptionId;
import org.springframework.data.repository.CrudRepository;

public interface OptionRepository extends CrudRepository<SurveyOption, SurveyOptionId> {
}
