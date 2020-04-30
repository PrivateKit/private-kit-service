package com.privatekit.server.entity;

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

    @Column(name = "option_label", nullable = false)
    private String optionLabel;

    @Column(name = "option_value", nullable = false)
    private String optionValue;

    @Column(name = "option_description")
    private String optionDescription;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "survey_id", nullable = false, referencedColumnName = "survey_id"),
            @JoinColumn(name = "option_key", nullable = false, referencedColumnName = "option_key")})
    private SurveyOption option;

    public SurveyOptionValue() {
    }

    public SurveyOptionValue(String optionLabel, String optionValue, String optionDescription, SurveyOption option) {
        this.optionLabel = optionLabel;
        this.optionValue = optionValue;
        this.optionDescription = optionDescription;
        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyOptionValue that = (SurveyOptionValue) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(optionLabel, that.optionLabel) &&
                Objects.equals(optionValue, that.optionValue) &&
                Objects.equals(optionDescription, that.optionDescription) &&
                Objects.equals(option, that.option);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, optionLabel, optionValue, optionDescription, option);
    }
}
