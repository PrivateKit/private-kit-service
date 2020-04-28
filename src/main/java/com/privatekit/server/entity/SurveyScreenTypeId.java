package com.privatekit.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SurveyScreenTypeId implements Serializable {

    private static final long serialVersionUID = 2316597833173208012L;

    @Column(name = "screen_type_key")
    private String screenTypeKey;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyScreenTypeId that = (SurveyScreenTypeId) o;
        return Objects.equals(screenTypeKey, that.screenTypeKey) &&
                Objects.equals(surveyId, that.surveyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenTypeKey, surveyId);
    }
}
