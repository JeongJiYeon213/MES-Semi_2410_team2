package org.zerock.b02.repository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.repository.search.InboundSearch;

import java.util.Optional;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long>, InboundSearch {

    Optional<Inbound> findTopByOrderByInboundCodeDesc();
}
