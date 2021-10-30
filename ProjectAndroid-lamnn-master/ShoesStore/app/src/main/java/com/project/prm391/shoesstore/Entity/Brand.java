package com.project.prm391.shoesstore.Entity;

import android.net.Uri;
import android.os.Parcel;

import java.io.Serializable;

/**
 * Brand of the shoes.
 */
public class Brand implements Serializable {
    /**
     * Brand ID.
     */
    private String id;

    /**
     * Brand name.
     */
    private String name = "";

    /**
     * Brand logo.
     */
    private String imageUrl = "";

    public Brand() {

    }

    public Brand(String id) {
        this.id = id;
    }

    public Brand(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    protected Brand(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readParcelable(Uri.class.getClassLoader());
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Brand brand = (Brand) o;

        return id != null ? id.equals(brand.id) : brand.id == null;
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
