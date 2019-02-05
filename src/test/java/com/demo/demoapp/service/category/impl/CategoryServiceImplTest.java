package com.demo.demoapp.service.category.impl;

import com.demo.demoapp.repository.CategoryRepository;
import com.demo.demoapp.service.category.CategoryService;
import com.demo.demoapp.service.category.domain.Category;
import com.demo.demoapp.web.CategoryRequest;
import com.demo.demoapp.web.CategoryTreeResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {

    private CategoryService service;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryTreeConverter categoryTreeConverter;

    @Before
    public void init() {
        service = new CategoryServiceImpl(categoryRepository,categoryTreeConverter);
    }

    @Test
    public void categoryService_createNewCategory_succeed() {
        String name = "category";
        String slug = "slug1";
        Boolean isVisible = true;
        CategoryRequest categoryRequest = new CategoryRequest(name, slug, isVisible);
        Category category =  new Category(name,slug,isVisible);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        service.addCategory(categoryRequest);
        verify(categoryRepository,times(1)).save(any());
    }
    @Test
    public void categoryService_createNewCategoryWithNonExistingParent_failed() {
        String name = "category";
        String slug = "slug1";
        Boolean isVisible = true;
        String parentCategory="NonExistingParent";
        CategoryRequest categoryRequest = new CategoryRequest(name, slug, isVisible,parentCategory);

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        Optional<Category> addedCategory = service.addCategory(categoryRequest);
        verify(categoryRepository,times(1)).findByName(anyString());
        assertThat(addedCategory).isEmpty();
    }
    @Test
    public void categoryService_updateVisibility_succeed(){
        Category category = new Category("", "", true);
        Optional<Category> categoryOptional = Optional.of(category);

        when(categoryRepository.findById(any(UUID.class))).thenReturn(categoryOptional);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        service.updateVisibility(false, category.getId().toString());
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).save(categoryOptional.get());
    }

    @Test
    public void categoryService_searchCategoryById_succeed(){
        Category category = new Category("", "", true);
        Optional<Category> categoryOptional = Optional.of(category);

        when(categoryRepository.findById(any(UUID.class))).thenReturn(categoryOptional);

        service.searchCategoryById(category.getId().toString());
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    public void categoryService_searchBySlug_succeed(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("", "", true));

        when(categoryRepository.findBySlug(anyString())).thenReturn(categoryList);

        service.searchCategoryBySlug("slug");
        verify(categoryRepository, times(1)).findBySlug("slug");
    }

    @Test
    public void categoryService_searchByName_succeed(){
        Category category = new Category("" , "" , true);
        String name = "name";

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));

        service.searchCategoryByName(name);
        verify(categoryRepository, times(1)).findByName(name);
    }

    @Test
    public void categoryService_searchSubcategoryTree_succeed(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("", "", true));
        CategoryTreeResponse ctr = new CategoryTreeResponse(UUID.randomUUID(), "", "", "");
        UUID parentId = UUID.randomUUID();

        when(categoryRepository.findByParentCategory(any(UUID.class))).thenReturn(categoryList);
        when(categoryTreeConverter.convert(anyList())).thenReturn(Optional.of(ctr));

        service.searchSubcategoryTree(parentId.toString());
        verify(categoryRepository, times(1)).findByParentCategory(parentId);
        verify(categoryTreeConverter, times(1)).convert(categoryList);
    }

}