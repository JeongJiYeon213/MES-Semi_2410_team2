package org.zerock.b02.repository;

import org.hibernate.annotations.Where;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.repository.search.InboundSearch;

import java.util.List;

@Repository
@SpringBootApplication
@EnableJpaAuditing
public interface InboundRepository extends JpaRepository<Inbound, Long>, InboundSearch {

    @Query("SELECT i FROM Inbound i WHERE i.inboundCode = :inboundCode")
    List<Inbound> findByInboundCode(@Param("inboundCode") String inboundCode);

    @Query("SELECT i FROM Inbound i WHERE i.productCode = :productCode")
    List<Inbound> findByProductCode(@Param("productCode") String productCode);

    @Query("SELECT i FROM Inbound i WHERE i.supplierId = :supplierId")
    List<Inbound> findBySupplierId(@Param("supplierId") String supplierId);

    @Query("SELECT i FROM Inbound i WHERE i.inboundStatus = :status")
    List<Inbound> findByStatus(@Param("status") String status);

    @Query("select i from Inbound i where i.inboundCode like concat('%', :keyword, '%')")
    Page<Inbound> findKeyword(@Param("keyword") String keyword, Pageable pageable);

    String findTopByOderByInboundCodeDesc();



}
