package com.demo.demoapp.web;

import com.demo.demoapp.service.category.CategoryService;
import com.demo.demoapp.service.category.domain.Category;
import com.demo.demoapp.service.category.impl.CategoryTreeConverter;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void categoryController_createCategory_succeed() throws Exception{
        Category category = new Category("category","",false);
        UUID categoryId = category.getId();
        Category parentCategory = new Category("parent","",false);

        when(categoryService.addCategory(any(CategoryRequest.class))).thenReturn(Optional.of(category));
        when(categoryService.searchCategoryByName(anyString())).thenReturn(Optional.of(parentCategory));

        this.mockMvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON_VALUE).content(generateCreateCategoryRequest()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(categoryId.toString()));
    }

    private String generateCreateCategoryRequest(){
        return "{ \"name\" : \"category\", \"slug\" : \"slug1\",\"is_visible\" :false,\"parent_category\" : \"parent\"}";
    }

    @Test
    public void categoryController_searchCategoryById_succeed() throws Exception{
        Category category = new Category("category", "slug1", true);
        UUID id = category.getId();

        when(categoryService.searchCategoryById(anyString())).thenReturn(Optional.of(category));

        this.mockMvc.perform(get("/api/categories?id="+id.toString()).contentType(MediaType.APPLICATION_JSON_VALUE).content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(generateGetByIdResponse(id.toString())));


    }

    private String generateGetByIdResponse(String id){
        return "[{ \"id\" : \"" + id + "\", \"name\" :\"category\",\"slug\" : \"slug1\", \"isVisible\" : true, \"parentcategory\" : null}]";
    }

    @Test
    public void categoryController_searchCategoryBySlug_succeed() throws Exception{
        String slug = "slug1";
        Category category1 = new Category("category1", slug, true);
        Category category2 = new Category("category2", slug, true);
        List<Category> cl = new ArrayList<>();
        cl.add(category1);
        cl.add(category2);

        when(categoryService.searchCategoryBySlug(anyString())).thenReturn(cl);

        this.mockMvc.perform(get("/api/categories?slug=" + slug).contentType(MediaType.APPLICATION_JSON_VALUE).content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(generateGetBySlugResponse(category1.getId().toString(), category2.getId().toString())));


    }

    private String generateGetBySlugResponse(String id1, String id2){
        return "[{\"id\":\"" + id1 + "\",\"name\":\"category1\",\"slug\":\"slug1\",\"isVisible\":true,\"parentcategory\":null}," +
                "{\"id\":\"" + id2 + "\",\"name\":\"category2\",\"slug\":\"slug1\",\"isVisible\":true,\"parentcategory\":null}]";
    }

    @Test
    public void categoryController_searchSubcategoryTree_succeed() throws Exception{
        CategoryTreeResponse root = new CategoryTreeResponse(UUID.randomUUID(), "root", "slug1", "");
        CategoryTreeResponse child1 = new CategoryTreeResponse(UUID.randomUUID(), "child1", "slug1", "root");
        CategoryTreeResponse child2 = new CategoryTreeResponse(UUID.randomUUID(), "child2", "slug1", "root");
        root.getChildrenTree().put(child1.getId(), child1);
        root.getChildrenTree().put(child2.getId(), child2);


        when(categoryService.searchSubcategoryTree(anyString())).thenReturn(Optional.of(root));

        this.mockMvc.perform(get("/api/categories/"+root.getId().toString()+"/tree")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(generateSubcategoryTreeResponse(root.getId().toString(), child1.getId().toString(), child2.getId().toString())));


    }

    private String generateSubcategoryTreeResponse(String id_root, String id_child1, String id_child2){
        return "{\"id\":\"" + id_root + "\",\"name\":\"root\",\"slug\":\"slug1\",\"parentCategory\":\"\",\"childrenList\":" +
                "[{\"id\":\"" + id_child1 + "\",\"name\":\"child1\",\"slug\":\"slug1\",\"parentCategory\":\"root\",\"childrenList\":[]}," +
                "{\"id\":\"" + id_child2 + "\",\"name\":\"child2\",\"slug\":\"slug1\",\"parentCategory\":\"root\",\"childrenList\":[]}]}";
    }

    @Test
    public void categoryController_updateCategoryVisibility_succeed() throws Exception{
        Category category = new Category("category", "slug1", true);

        when(categoryService.updateVisibility(anyBoolean(), anyString())).thenReturn(Optional.of(category));

        this.mockMvc.perform(patch("/api/categories/" + category.getId().toString() + "?is_visible=true")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(generateUpdateCategoryVisibilityResponse(category.getId().toString())));
    }

    private String generateUpdateCategoryVisibilityResponse(String id){
        return "{ \"id\" : \"" + id + "\", \"name\" :\"category\",\"slug\" : \"slug1\", \"isVisible\" : true, \"parentcategory\" : null}";
    }
}