package com.privatekit.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "survey_options")
public class SurveyOption implements Serializable {

    private static final long serialVersionUID = 6203941080989251006L;

    @EmbeddedId
    private SurveyOptionId id;

    @OneToMany(mappedBy = "option", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<SurveyOptionValue> values;

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
