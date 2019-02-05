package com.demo.demoapp.service.category.impl;

import com.demo.demoapp.Application;
import com.demo.demoapp.service.category.domain.Category;
import com.demo.demoapp.web.CategoryTreeResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class CategoryTreeConverterTest {

    private CategoryTreeConverter categoryTreeConverter = new CategoryTreeConverter();

    private Category root;
    private Category child1;
    private Category child2;
    private Category child1_1;

    @Before
    public void setUpCategoryData(){
        root = new Category("root", "slug", true);
        child1 = new Category("child1", "slug", true, root);
        child2 = new Category("child2", "slug", true, root);
        child1_1 = new Category("child1_1", "slug", true, child1) ;
    }

    @Test
    public void categoryTreeConverter_convertListToTree_succeed() {
        List<Category> subCategoryList = new ArrayList<>();
        subCategoryList.add(root);
        subCategoryList.add(child1);
        subCategoryList.add(child2);
        subCategoryList.add(child1_1);

        Optional<CategoryTreeResponse> ctr = categoryTreeConverter.convert(subCategoryList);

        assertThat(ctr.isPresent()).isTrue();
        assertThat(ctr.get().getId()).isEqualTo(root.getId());
        assertThat(ctr.get().getChildrenTree().get(child1.getId()).getId()).isEqualTo(child1.getId());
        assertThat(ctr.get().getChildrenTree().get(child2.getId()).getId()).isEqualTo(child2.getId());

        CategoryTreeResponse child1Tree = ctr.get().getChildrenTree().get(child1.getId());
        assertThat(child1Tree.getChildrenTree().get(child1_1.getId()).getId()).isEqualTo(child1_1.getId());
    }
}