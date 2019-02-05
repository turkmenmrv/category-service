package com.demo.demoapp.web;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CategoryTreeResponse {
    private UUID id;

    private String name;

    private String slug;

    private String parentCategory;

    @JsonIgnore
    private Map<UUID, CategoryTreeResponse> childrenTree = new HashMap<>();

    private CategoryTreeResponse[] childrenList;

    public CategoryTreeResponse(UUID id, String name, String slug, String parentCategory){
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parentCategory = parentCategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setChildrenTree(Map<UUID, CategoryTreeResponse> childrenTree) {
        this.childrenTree = childrenTree;
    }

    public Map<UUID, CategoryTreeResponse> getChildrenTree() {
        return childrenTree;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public CategoryTreeResponse[] getChildrenList() {
        childrenList = this.childrenTree.values().toArray(new CategoryTreeResponse[0]);
        return childrenList;
    }


}
