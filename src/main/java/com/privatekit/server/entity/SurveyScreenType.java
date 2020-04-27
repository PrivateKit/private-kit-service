package com.privatekit.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "survey_screen_types")
public class SurveyScreenType implements Serializable {

    private static final long serialVersionUID = 3230287918120991215L;

    @EmbeddedId
    private SurveyScreenTypeId id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyScreenType that = (SurveyScreenType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
