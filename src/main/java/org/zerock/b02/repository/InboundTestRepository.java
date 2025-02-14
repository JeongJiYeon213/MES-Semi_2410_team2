package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b02.domain.InboundTest;

public interface InboundTestRepository extends JpaRepository<InboundTest, Long> {
}
