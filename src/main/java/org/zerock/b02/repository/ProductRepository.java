package org.zerock.b02.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b02.dto.ProductDTO;
import org.zerock.b02.repository.search.ProductSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {


	@Query("SELECT p FROM Product p WHERE p.productName = :productName")
	List<Product> findByProductName(@Param("productName") String productName);

	@Query("SELECT p FROM Product p WHERE p.productCode = :productCode")
	List<Product> findByProductCode(@Param("productCode") String productCode);

	@Query("select p from Product p where p.productName like concat('%', :keyword, '%')")
	Page<Product> findKeyword(@Param("keyword") String keyword, Pageable pageable);







}
