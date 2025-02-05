package org.zerock.b02.repository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.query.Param;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.repository.search.InboundSearch;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public interface InboundRepository extends JpaRepository<Inbound, Long>, InboundSearch {
    @Query("select b from Inbound b where b.productId = :productId and b.supplierId = :supplierId")
    List<Inbound> findByProductIdAndSupplierId(@Param("productId") Long productId, @Param("supplierId") Long supplierId);

    @Query("select b from Inbound b where b.supplierId in :supplierId")
    List<Inbound> findBySupplierIdIn(@Param("supplierId") List<Long> supplierId);

    @Query("select b from Inbound b where b.description like concat('%', :keyword, '%')")
    Page<Inbound> findKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select b from Inbound b where cast(b.productId as string) like concat('%', :keyword, '%') order by b.inboundId desc")
    Page<Inbound> findByProductIdContainingOrderByInboundIdDesc(@Param("keyword") String keyword, Pageable pageable);
}
