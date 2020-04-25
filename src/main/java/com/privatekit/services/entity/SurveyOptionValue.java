package com.privatekit.services.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "survey_option_values")
public class SurveyOptionValue implements Serializable {

    private static final long serialVersionUID = 101017407132132276L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "survey_id", nullable = false)
    private Integer surveyId;

    @Column(name = "option_key", nullable = false)
    private String optionKey;

    @Column(name = "option_label", nullable = false)
    private String optionLabel;

    @Column(name = "option_value", nullable = false)
    private String optionValue;

    @Column(name = "option_description", nullable = false)
    private String optionDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyOptionValue that = (SurveyOptionValue) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
