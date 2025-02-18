package org.zerock.b02.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b02.domain.OutboundTest;

import javax.transaction.Transactional;

public interface OutboundTestRepository extends JpaRepository<OutboundTest, Long> {
	@Modifying
	@Transactional
	@Query("DELETE FROM OutboundTest o WHERE o.stock.stockId = :stockId")
	void deleteByStockId(@Param("stockId") Long stockId);

}
