package com.privatekit.services.repository;

import com.privatekit.services.entity.InputType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputTypeRepository extends CrudRepository<InputType, Integer> {
}
