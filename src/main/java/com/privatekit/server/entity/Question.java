package com.privatekit.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class Question implements Serializable {

    @EmbeddedId
    private QuestionId id;

    @Column(name = "question_text")
    private String text;

    @Column(name = "question_image")
    private String image;

    @Column(name = "question_type", nullable = false)
    private String type;

    @Column(name = "question_required")
    private Boolean required;

    @Column(name = "screen_type_key")
    private String screenType;

    @Column(name = "option_key")
    private String optionKey;

    @Column(name = "survey_id", updatable = false, insertable = false)
    private Integer surveyId;

    public static Question from(com.privatekit.server.controller.model.Question q) {
        final Question question = new Question();
        final QuestionId id = new QuestionId();
        id.setQuestionKey(q.getQuestionKey());
        question.setId(id);
        question.setText(q.getQuestionText());
        question.setImage(q.getImage());
        question.setType(q.getQuestionType());
        question.setRequired(q.isRequired());
        question.setScreenType(q.getScreenType());
        question.setOptionKey(q.getOptionKey());
        return question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
