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
public class QuestionConditionId implements Serializable {

    private static final long serialVersionUID = 4078898608910106424L;

    @Column(name = "response")
    private String response;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "question_key")
    private Integer questionKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionConditionId that = (QuestionConditionId) o;
        return Objects.equals(response, that.response) &&
                Objects.equals(surveyId, that.surveyId) &&
                Objects.equals(questionKey, that.questionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(response, surveyId, questionKey);
    }
}
