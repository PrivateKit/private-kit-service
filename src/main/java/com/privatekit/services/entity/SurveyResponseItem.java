package com.privatekit.services.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "survey_item_responses")
public class SurveyResponseItem implements Serializable {

    private static final long serialVersionUID = -4886128646505421621L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "survey_id", nullable = false)
    private Integer surveyId;

    @Column(name = "question_key", nullable = false)
    private Integer questionKey;

    @Column(name = "survey_item_response_value", nullable = false)
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyResponseItem that = (SurveyResponseItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
