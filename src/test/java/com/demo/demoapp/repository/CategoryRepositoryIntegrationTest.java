package com.demo.demoapp.repository;

import com.demo.demoapp.Application;
import com.demo.demoapp.service.category.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@Transactional
public class CategoryRepositoryIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category root1;
    private Category root2;
    private Category child1_1;
    private Category child1_2;
    private Category child1_1_1;
    private Category child1_1_2;
    private Category child1_1_3;
    private Category child1_2_1;


    @Before
    public void setUpTestData() throws Exception{
        root1 = createTestCategory("root1", "slugX", true, "");
        root2 = createTestCategory("root2", "slugX", true, "");
        child1_1 = createTestCategory("child1_1", "slugY", true, "root1");
        child1_2 = createTestCategory("child1_2", "slugY", false, "root1");
        child1_1_1 = createTestCategory("child1_1_1", "slugZ", true, "child1_1");
        child1_1_2 = createTestCategory("child1_1_2", "slugZ", true, "child1_1");
        child1_1_3 = createTestCategory("child1_1_3", "slugZ", true, "child1_1");
        child1_2_1 = createTestCategory("child1_2_1", "slugZ", false, "child1_2");
    }

    private Category createTestCategory(String name, String slug, boolean isVisible, String parentName) throws Exception{
        Category newCategory = new Category(name, slug, isVisible);
        if(parentName!=null && !parentName.isEmpty()){
            Optional<Category> parentCategory = categoryRepository.findByName(parentName);
            if(parentCategory.isPresent()) {
                newCategory.setParentcategory(parentCategory.get());
            }
            else{
                throw new Exception();
            }
        }
        return categoryRepository.save(newCategory);
    }

    @Test
    public void categoryRepository_createNewCategory_succeed(){
        Category category = new Category("name","",true);
        Category saved = categoryRepository.save(category);
        Optional<Category> read = categoryRepository.findById(saved.getId());
        assertThat(saved).isEqualTo(read.get());
    }

    @Test
    public void categoryRepository_findItemById_succeed(){
        UUID id = root2.getId();
        Optional<Category> result = categoryRepository.findById(root2.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(root2);
    }

    @Test
    public void categoryRepository_findItemByName_succeed(){
        String searchName = "child1_1";
        Optional<Category> category = categoryRepository.findByName(searchName);
        assertThat(category.isPresent()).isTrue();
        assertThat(category.get()).isEqualTo(child1_1);
    }

    @Test
    public void categoryRepository_findItemByNonExistingName_fails(){
        String searchName = "root3";
        Optional<Category> category = categoryRepository.findByName(searchName);
        assertThat(category.isPresent()).isFalse();
    }

    @Test
    public void categoryRepository_findItemsBySlug_succeed(){
        String searchSlug = "slugX";
        List<Category> resultList = categoryRepository.findBySlug(searchSlug);
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList).contains(root1, root2);
    }

    @Test
    public void categoryRepository_findItemByNonexistingSlug_returnEmptyList(){
        String searchSlug = "slugNO";
        List<Category> resultList = categoryRepository.findBySlug(searchSlug);
        assertThat(resultList).isEmpty();
    }

    @Test
    public void categoryRepository_findSubcategoryTreeFromRoot_succeed(){
        List<Category> resultList = categoryRepository.findByParentCategory(root1.getId());

        List<Category> subCategoriesOfRoot1 = new ArrayList<>();
        subCategoriesOfRoot1.add(root1);
        subCategoriesOfRoot1.add(child1_1);
        subCategoriesOfRoot1.add(child1_2);
        subCategoriesOfRoot1.add(child1_1_1);
        subCategoriesOfRoot1.add(child1_1_2);
        subCategoriesOfRoot1.add(child1_1_3);
        subCategoriesOfRoot1.add(child1_2_1);

        assertThat(subCategoriesOfRoot1).containsExactlyInAnyOrder(resultList.toArray(new Category[0]));
    }

    @Test
    public void categoryRepository_findSubcategoryTreeInBetween_succeed(){
        List<Category> resultList = categoryRepository.findByParentCategory(child1_2.getId());

        List<Category> subCategoriesOfChild1_2 = new ArrayList<>();
        subCategoriesOfChild1_2.add(child1_2);
        subCategoriesOfChild1_2.add(child1_2_1);

        assertThat(subCategoriesOfChild1_2).containsExactlyInAnyOrder(resultList.toArray(new Category[0]));
    }

}