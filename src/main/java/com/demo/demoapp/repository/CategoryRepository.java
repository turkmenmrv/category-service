package com.demo.demoapp.repository;

import com.demo.demoapp.service.category.domain.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends CrudRepository<Category,UUID> {
    List<Category> findBySlug(String slug);
    Optional<Category> findByName(String name);

    @Query(value = "with recursive category_tree " +
            "as( " +
            "select id, name, slug, is_visible, parentcategory_id from demo.category " +
            "where id = ?1 " +
            "union all " +
            "select c.id, c.name, c.slug, c.is_visible, c.parentcategory_id " +
            "from demo.category c, category_tree t " +
            "where t.id = c.parentcategory_id " +
            ") " +
            "select * from category_tree", nativeQuery = true)
    List<Category> findByParentCategory(UUID parentcategory);
}
