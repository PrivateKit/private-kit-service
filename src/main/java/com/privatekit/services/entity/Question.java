package com.privatekit.services.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "questions")
public class Question implements Serializable {

    private static final long serialVersionUID = 4234176014109958141L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "survey_section_id", nullable = false)
    private Integer surveySectionId;

    @Column(name = "input_type_id", nullable = false)
    private Integer inputTypeId;

    @Column(name = "question_name", nullable = false)
    private String name;

    @Column(name = "question_subtext")
    private String subtext;

    @Column(name = "answer_required_yn")
    private Boolean answerRequired;

    @Column(name = "option_group_id")
    private Integer optionGroupId;

    @Column(name = "allow_multiple_option_answers_yn")
    private Boolean allowMultipleOptionAnswer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSurveySectionId() {
        return surveySectionId;
    }

    public void setSurveySectionId(Integer surveySectionId) {
        this.surveySectionId = surveySectionId;
    }

    public Integer getInputTypeId() {
        return inputTypeId;
    }

    public void setInputTypeId(Integer inputTypeId) {
        this.inputTypeId = inputTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public Boolean getAnswerRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(Boolean answerRequired) {
        this.answerRequired = answerRequired;
    }

    public Integer getOptionGroupId() {
        return optionGroupId;
    }

    public void setOptionGroupId(Integer optionGroupId) {
        this.optionGroupId = optionGroupId;
    }

    public Boolean getAllowMultipleOptionAnswer() {
        return allowMultipleOptionAnswer;
    }

    public void setAllowMultipleOptionAnswer(Boolean allowMultipleOptionAnswer) {
        this.allowMultipleOptionAnswer = allowMultipleOptionAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
