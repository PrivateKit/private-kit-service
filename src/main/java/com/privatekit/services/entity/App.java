package com.privatekit.services.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "apps")
public class App implements Serializable {

    @EmbeddedId
    private AppId id;

    @Column(name = "app_status", nullable = false)
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return Objects.equals(id, app.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
