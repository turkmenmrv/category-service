package com.demo.demoapp.web;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryRequest {

    private String name;

    private String slug;

    private boolean isVisible;

    private String parentCategory;

    public CategoryRequest(){}

    public CategoryRequest(String name, String slug, Boolean isVisible){
        this(name,slug,isVisible,"");
    }
    public CategoryRequest(String name, String slug, Boolean isVisible, String parentCategory){
        this.name = name;
        this.slug = slug;
        this.isVisible = isVisible;
        this.parentCategory = parentCategory;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    @JsonProperty(value="parent_category")
    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public boolean getVisible() {
        return isVisible;
    }

    @JsonProperty(value="is_visible")
    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
