package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b02.domain.Customer;
import org.zerock.b02.repository.search.CustomerSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, CustomerSearch {

   @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
   List<Customer> findByCustomerId(@Param("customerId") String CustomerId);

}