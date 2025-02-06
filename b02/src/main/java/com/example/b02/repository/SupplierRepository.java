package com.example.b02.repository;

import com.example.b02.domain.Supplier;
import com.example.b02.repository.search.SupplierSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long>, SupplierSearch {

}