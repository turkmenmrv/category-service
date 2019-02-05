package com.demo.demoapp.service.category;

import com.demo.demoapp.service.category.domain.Category;
import com.demo.demoapp.web.CategoryRequest;
import com.demo.demoapp.web.CategoryTreeResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> addCategory(CategoryRequest categoryRequest);
    Optional<Category> updateVisibility(Boolean isVisible, String id);
    Optional<Category> searchCategoryById(String id);
    List<Category> searchCategoryBySlug(String slug);
    Optional<Category> searchCategoryByName(String name);
    Optional<CategoryTreeResponse> searchSubcategoryTree(String id);
}
