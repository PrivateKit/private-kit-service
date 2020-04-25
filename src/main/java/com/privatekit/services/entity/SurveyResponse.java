package com.privatekit.services.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "survey_responses")
public class SurveyResponse implements Serializable {

    private static final long serialVersionUID = 5552137421916894622L;

    @EmbeddedId
    private SurveyResponseId id;

    @Column(name = "skipped", nullable = false)
    private Boolean skipped;

    @OneToMany(mappedBy="response", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<SurveyResponseItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyResponse that = (SurveyResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(skipped, that.skipped);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skipped);
    }
}
