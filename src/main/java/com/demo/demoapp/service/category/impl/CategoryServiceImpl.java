package com.demo.demoapp.service.category.impl;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.demo.demoapp.repository.CategoryRepository;
import com.demo.demoapp.service.category.CategoryService;
import com.demo.demoapp.service.category.domain.Category;
import com.demo.demoapp.web.CategoryRequest;
import com.demo.demoapp.web.CategoryTreeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryTreeConverter categoryTreeConverter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,CategoryTreeConverter categoryTreeConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryTreeConverter = categoryTreeConverter;
    }

    @Override
    public Optional<Category> addCategory(CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.getName(), categoryRequest.getSlug(), categoryRequest.getVisible());
        try{
            //check if parent is assigned in request
            if(categoryRequest.getParentCategory()!=null && !categoryRequest.getParentCategory().isEmpty()){
                Optional<Category> parentCategory = searchCategoryByName(categoryRequest.getParentCategory());
                parentCategory.map(c->{
                    category.setParentcategory(c);
                    categoryRepository.save(category);
                    return category;
                }).orElseThrow(()->new IllegalArgumentException("Parent Category not found"));
            }
            return Optional.of(categoryRepository.save(category));
        }
        catch(Exception e){//Exception from database in case of conflicting names
            return Optional.empty();
        }
    }

    @Override
    public Optional<Category> updateVisibility(Boolean isVisible, String id) {
        Optional<Category> c = categoryRepository.findById(UUID.fromString(id));
        return c.map((Category category) -> {
            category.setIsVisible(isVisible);
             return Optional.of(categoryRepository.save(category));})
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Category> searchCategoryById(String id) {
         return categoryRepository.findById(UUID.fromString(id));
    }

    @Override
    public List<Category> searchCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    public Optional<Category> searchCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Optional<CategoryTreeResponse> searchSubcategoryTree(String id) {
        List<Category> subcategoryList = categoryRepository.findByParentCategory(UUID.fromString(id));
        return categoryTreeConverter.convert(subcategoryList);
    }
}
