package org.zerock.b02.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b02.domain.InboundTest;

import javax.transaction.Transactional;

public interface InboundTestRepository extends JpaRepository<InboundTest, Long> {
	@Modifying
	@Transactional
	@Query("DELETE FROM InboundTest i WHERE i.stock.stockId = :stockId")
	void deleteByStockId(@Param("stockId") Long stockId);

}
