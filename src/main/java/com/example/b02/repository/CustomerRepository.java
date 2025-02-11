package com.example.b02.repository;

import com.example.b02.domain.Customer;
import com.example.b02.repository.search.CustomerSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerSearch {

    List<Customer> findAll();

    List<Customer> findDistinctByCustomerIdAndCustomerNameAndCustomerInfo();
}