package com.privatekit.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "surveys")
public class Survey implements Serializable {

    private static final long serialVersionUID = 4404232346150316646L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "survey_name", nullable = false)
    private String name;

    @Column(name = "survey_language")
    private String surveyLang;

    @Column(name = "survey_description")
    private String description;

    @Column(name = "survey_image")
    private String image;

    @Column(name = "app_namespace", nullable = false)
    private String appNamespace;

    @Column(name = "app_key", nullable = false)
    private String appKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id) &&
                Objects.equals(name, survey.name) &&
                Objects.equals(surveyLang, survey.surveyLang) &&
                Objects.equals(description, survey.description) &&
                Objects.equals(image, survey.image) &&
                Objects.equals(appNamespace, survey.appNamespace) &&
                Objects.equals(appKey, survey.appKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, appNamespace, appKey);
    }

    public static Survey from(com.privatekit.server.controller.model.Survey s) {

        final Survey survey = new Survey();
        survey.setName(s.getName());
        survey.setDescription(s.getDescription());
        survey.setImage(s.getImage());
        survey.setSurveyLang(s.getSurveyLanguage());

        return survey;
    }
}
