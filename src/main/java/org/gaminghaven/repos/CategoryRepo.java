package org.gaminghaven.repos;

import org.gaminghaven.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    @Query("select c from Category c where LOWER(c.name) = LOWER(:name)")
    Category findByName(@Param("name") String name);
}
