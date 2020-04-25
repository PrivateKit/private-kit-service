package com.privatekit.services.repository;

import com.privatekit.services.entity.Survey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface SurveyRepository extends CrudRepository<Survey, Integer> {

    Collection<Survey> findByAppNamespace(String appNamespace);

    @Query(value="SELECT s FROM Survey s WHERE s.appNamespace = ?1 AND s.id=?2")
    Optional<Survey> find(String appNamespace, int surveyId);
}
