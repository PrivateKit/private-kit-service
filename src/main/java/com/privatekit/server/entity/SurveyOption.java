package com.privatekit.server.entity;

import com.privatekit.server.controller.model.Option;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "survey_option_groups")
public class SurveyOption implements Serializable {

    private static final long serialVersionUID = 6203941080989251006L;

    @EmbeddedId
    private SurveyOptionId id;

    @OneToMany(mappedBy = "option", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<SurveyOptionValue> values;

    public static SurveyOption from(Option option) {
        final SurveyOption surveyOption = new SurveyOption();
        final SurveyOptionId id = new SurveyOptionId();

        id.setOptionKey(option.getKey());

        surveyOption.setId(id);

        surveyOption.setValues(
        option.getValues().stream().map(v->{
            final SurveyOptionValue value = new SurveyOptionValue();
            value.setOptionDescription(v.getDescription());
            value.setOptionLabel(v.getLabel());
            value.setOptionValue(v.getValue());
            value.setOption(surveyOption);
            return value;
        }).collect(Collectors.toSet()));

        return surveyOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyOption that = (SurveyOption) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
