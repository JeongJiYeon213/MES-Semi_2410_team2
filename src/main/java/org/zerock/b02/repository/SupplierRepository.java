package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b02.domain.Supplier;
import org.zerock.b02.repository.search.SupplierSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String>, SupplierSearch {
    Optional<Supplier> findBySupplierId(String supplierId);
}