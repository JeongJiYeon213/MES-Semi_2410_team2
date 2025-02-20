package org.zerock.b02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.repository.search.InboundSearch;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long>, InboundSearch {

    Optional<Inbound> findTopByOrderByInboundCodeDesc();

    // from과 to 사이의 날짜로 조회
    Page<Inbound> findByInboundDateBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    // from 이후의 날짜로 조회
    Page<Inbound> findByInboundDateAfter(LocalDateTime from, Pageable pageable);

    // to 이전의 날짜로 조회
    Page<Inbound> findByInboundDateBefore(LocalDateTime to, Pageable pageable);
}
