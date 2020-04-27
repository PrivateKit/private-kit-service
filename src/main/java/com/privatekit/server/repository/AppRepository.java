package com.privatekit.server.repository;

import com.privatekit.server.entity.App;
import com.privatekit.server.entity.AppId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppRepository extends CrudRepository<App, AppId> {

    Optional<App> findById_NamespaceAndAndStatus(Integer namespace, String status);
}