package com.privatekit.services.entity;

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
        return Objects.equals(id, survey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
