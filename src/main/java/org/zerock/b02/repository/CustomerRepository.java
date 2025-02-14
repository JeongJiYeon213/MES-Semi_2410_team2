package org.zerock.b02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b02.domain.Customer;
import org.zerock.b02.repository.search.CustomerSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, CustomerSearch {

   @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
   List<Customer> findByCustomerId(@Param("customerId") String CustomerId);


   // 드롭다운 검색 기능
   @Query("SELECT c FROM Customer c " +
           "WHERE (:keyword IS NULL OR c.customerId LIKE %:keyword%) " +
           "AND (:customerId IS NULL OR c.customerId = :customerId)")
   Page<Customer> searchWithFilters(@Param("keyword") String keyword,
                                    @Param("customerId") String customerId,
                                    Pageable pageable);
}