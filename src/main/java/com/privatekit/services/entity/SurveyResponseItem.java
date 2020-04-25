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

    @Column(name = "survey_item_response_value", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "survey_id", nullable = false),
            @JoinColumn(name = "question_key", nullable = false)})
    private SurveyResponse response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyResponseItem that = (SurveyResponseItem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(value, that.value) &&
                Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, response);
    }
}
