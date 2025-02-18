package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Outbound;
import org.zerock.b02.repository.search.OutboundSearch;

import java.util.Optional;

@Repository
public interface OutboundRepository extends JpaRepository<Outbound, Long>, OutboundSearch {

    Optional<Outbound> findTopByOrderByOutboundCodeDesc();
}
