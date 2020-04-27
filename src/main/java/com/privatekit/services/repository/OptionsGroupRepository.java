package com.privatekit.services.repository;

import com.privatekit.services.entity.OptionsGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsGroupRepository extends CrudRepository<OptionsGroup, Integer> {
}
