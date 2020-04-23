package com.privatekit.services.repository;

import com.privatekit.services.entity.OptionChoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionChoiceRepository extends CrudRepository<OptionChoice, Integer> {
}
