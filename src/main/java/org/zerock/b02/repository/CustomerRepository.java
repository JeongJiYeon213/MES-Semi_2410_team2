package org.zerock.b02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b02.domain.Customer;
import org.zerock.b02.repository.search.CustomerSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, CustomerSearch {

   // customerId로 검색 (페이징 포함)
   // @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
   // Page<Customer> findByCustomerId(@Param("customerId") String customerId,
   //                                 Pageable pageable);

      Optional<Customer> findByCustomerId(String customerName);
   
}
