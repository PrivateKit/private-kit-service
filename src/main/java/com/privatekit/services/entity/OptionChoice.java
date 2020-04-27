package com.privatekit.services.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "option_choices")
public class OptionChoice implements Serializable {

    private static final long serialVersionUID = 1370612781276171768L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "option_group_id", nullable = false)
    private Integer optionGroupId;

    @Column(name = "option_choice_name", nullable = false)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOptionGroupId() {
        return optionGroupId;
    }

    public void setOptionGroupId(Integer optionGroupId) {
        this.optionGroupId = optionGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionChoice that = (OptionChoice) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
