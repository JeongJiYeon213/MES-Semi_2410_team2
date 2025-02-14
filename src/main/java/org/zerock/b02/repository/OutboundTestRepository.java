package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b02.domain.OutboundTest;

public interface OutboundTestRepository extends JpaRepository<OutboundTest, Long> {
}
