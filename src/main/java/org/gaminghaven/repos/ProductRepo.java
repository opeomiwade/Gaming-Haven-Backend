package org.gaminghaven.repos;

import org.gaminghaven.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p where LOWER(p.category.name) = LOWER(:name)")
    List<Product> findByCategoryName(@Param("name") String categoryName);

    Product findByProductName(@Param("productName") String productName);

    @Query("select distinct LOWER(manufacturer) from Product")
    List<String> getManufacturers();

}
