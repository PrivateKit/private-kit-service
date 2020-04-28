package com.privatekit.server.repository;

import com.privatekit.server.entity.Survey;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface SurveyRepository extends CrudRepository<Survey, Integer> {

    Collection<Survey> findByAppNamespace(String appNamespace);

    Optional<Survey> findByAppNamespaceAndId(String appNamespace, int surveyId);
}
