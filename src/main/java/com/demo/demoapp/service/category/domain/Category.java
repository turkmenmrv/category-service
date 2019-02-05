package com.demo.demoapp.service.category.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(schema = "demo", name = "category")
public class Category  implements Serializable {
    @Id
    private UUID id;

    private String name;

    private String slug;

    private Boolean isVisible;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Category parentcategory;

    public Category() {
    }

    public Category(String name, String slug, Boolean isVisible) {
        id = UUID.randomUUID();
        this.name = name;
        this.slug = slug;
        this.isVisible = isVisible;
    }

    public Category(String name, String slug, Boolean isVisible, Category parentCategory) {
        id = UUID.randomUUID();
        this.name = name;
        this.slug = slug;
        this.isVisible = isVisible;
        this.parentcategory = parentCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    public Category getParentcategory() {
        return parentcategory;
    }

    public void setParentcategory(Category parentcategory) {
        this.parentcategory = parentcategory;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }
}
