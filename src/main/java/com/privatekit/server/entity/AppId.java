package com.privatekit.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class AppId implements Serializable {

    @Column(name = "app_namespace")
    private String namespace;

    @Column(name = "app_key")
    private String key;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppId appId = (AppId) o;
        return Objects.equals(namespace, appId.namespace) &&
                Objects.equals(key, appId.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }
}
