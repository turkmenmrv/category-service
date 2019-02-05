package com.demo.demoapp.web;

import com.demo.demoapp.service.category.CategoryService;
import com.demo.demoapp.service.category.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/categories" ,
            consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCategory(@RequestBody CategoryRequest categoryRequest){
        return categoryService.addCategory(categoryRequest).map(category -> ResponseEntity.ok(category.getId().toString()))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/categories",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> searchCategory(@RequestParam(value = "id", required = false) String id,
                                                         @RequestParam(value = "slug", required = false) String slug){
        if(isNotEmpty(id)){
            return categoryService.searchCategoryById(id)//
                    .map(category -> ResponseEntity.ok(Arrays.asList(category)))//
                    .orElse(ResponseEntity.notFound().build());//
        }
        if(isNotEmpty(slug)) {
            return ResponseEntity.ok(categoryService.searchCategoryBySlug(slug));
        }

        //all of id, slug and parentId should not be empty
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/categories/{id}/tree",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryTreeResponse> searchSubcategoryTree(@PathVariable(value = "id") String id){
        return categoryService.searchSubcategoryTree(id)//
                .map(subcategoryTree -> ResponseEntity.ok(subcategoryTree))//
                .orElse(ResponseEntity.notFound().build());//
    }

    @PatchMapping(value = "/categories/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> updateCategoryVisibility(@RequestParam("is_visible") Boolean categoryIsVisible,
                                                     @PathVariable("id") String id){
        return categoryService.updateVisibility(categoryIsVisible, id)
                .map(category -> ResponseEntity.ok(category))
                .orElse(ResponseEntity.badRequest().build());
    }

    private boolean isNotEmpty(String id) {
        return id != null && !id.isEmpty();
    }
}
