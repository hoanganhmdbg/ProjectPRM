package com.project.prm391.shoesstore.Entity;

import java.io.Serializable;

/**
 * Gender that the shoes target (e.g. Men, Women, Kids).
 */
public class Gender implements Serializable {
    /**
     * Gender ID
     */
    private String id;
    /**
     * Gender name
     */
    private String name = "";

    public Gender() {
    }

    public Gender(String id) {
        this.id = id;
    }

    public Gender(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        Gender gender = (Gender) o;

        return id != null ? id.equals(gender.id) : gender.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
