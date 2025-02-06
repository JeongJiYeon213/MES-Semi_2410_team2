package com.example.b02.repository;

import com.example.b02.domain.Customer;
import com.example.b02.repository.search.CustomerSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerSearch {

    @Query("SELECT c FROM Customer c ORDER BY c.bno DESC")
    List<Customer> findAllByCreatedAtDesc();

    @Query("select c from Customer c where c.customerId = :customerId and c.customerName = :customerName")
    List<Customer> findFromCustomerIdAndName(@Param("customerId") String customerId, @Param("customerName") String customerName);

    @Query("select c from Customer c where c.customerName in :customerNames")
    List<Customer> findFromCustomerNames(@Param("customerNames") List<String> customerNames);

    @Query("select c from Customer c where c.customerId like concat('%', :keyword, '%')")
    Page<Customer> findCustomerByKeyword(@Param("keyword") String keyword, Pageable pageable);

}