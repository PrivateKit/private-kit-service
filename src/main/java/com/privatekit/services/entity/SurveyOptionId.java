package com.privatekit.services.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SurveyOptionId implements Serializable {

    private static final long serialVersionUID = 1079207340181517441L;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "option_key")
    private Integer optionKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyOptionId that = (SurveyOptionId) o;
        return Objects.equals(surveyId, that.surveyId) &&
                Objects.equals(optionKey, that.optionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyId, optionKey);
    }
}
