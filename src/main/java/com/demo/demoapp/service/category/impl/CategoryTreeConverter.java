package com.demo.demoapp.service.category.impl;

import com.demo.demoapp.service.category.domain.Category;
import com.demo.demoapp.web.CategoryTreeResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class CategoryTreeConverter implements Converter<List<Category>, Optional<CategoryTreeResponse>> {
    @Override
    public Optional<CategoryTreeResponse> convert(List<Category> categoryList) {
        if(categoryList.isEmpty()){
            return Optional.empty();
        }

        Category rootCategory = categoryList.get(0);
        CategoryTreeResponse ctr = new CategoryTreeResponse(rootCategory.getId(), rootCategory.getName(), rootCategory.getSlug(),
                rootCategory.getParentcategory() == null ? "" : rootCategory.getParentcategory().getName());


        for(int i = 1; i < categoryList.size(); i++){
            addCategoryToTree(categoryList.get(i), ctr);
        }
        return Optional.of(ctr);
    }

    private void addCategoryToTree(Category category, CategoryTreeResponse treeResponse){
        Stack<Category> parentStack = new Stack<>();
        Category tmp = category.getParentcategory();
        while(tmp != null){
            parentStack.push(tmp);
            if(tmp.getId().equals(treeResponse.getId())){
                break;
            }
            tmp = tmp.getParentcategory();
        }

        if(parentStack.isEmpty() || !parentStack.pop().getId().equals(treeResponse.getId())){
            return;
        }

        CategoryTreeResponse directParent = treeResponse;
        while(!parentStack.empty()){
            Category parent = parentStack.pop();
            directParent = directParent.getChildrenTree().get(parent.getId());
        }
        directParent.getChildrenTree().put(category.getId(),
                new CategoryTreeResponse(category.getId(), category.getName(), category.getSlug(), category.getParentcategory().getName()));

    }
}
