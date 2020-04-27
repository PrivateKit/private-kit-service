package com.privatekit.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "screen_types")
public class ScreenType implements Serializable {

    private static final long serialVersionUID = -6247987332831325645L;

    @Id
    @Column(name = "screen_type_key")
    private String id;

    @Column(name = "screen_type_description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenType that = (ScreenType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
