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
public class QuestionId implements Serializable {

    private static final long serialVersionUID = 4658267388972606826L;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "question_key")
    private Integer questionKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionId that = (QuestionId) o;
        return Objects.equals(surveyId, that.surveyId) &&
                Objects.equals(questionKey, that.questionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyId, questionKey);
    }
}
