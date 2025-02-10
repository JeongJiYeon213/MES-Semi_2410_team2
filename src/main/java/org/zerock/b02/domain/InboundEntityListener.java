package org.zerock.b02.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;

public class InboundEntityListener {

    @PersistenceContext
    private EntityManager entityManager;

    @PrePersist
    public void prePersist(Inbound inbound) {
        if (inbound.getInboundCode() == null || inbound.getInboundCode().isEmpty()) {
            // inboundCode가 비어 있으면 자동 생성
            inbound.setInboundCode(generateInboundCode());
        }
    }

    private String generateInboundCode() {
        String prefix = "i";

        // 최근 inboundCode 조회
        String query = "SELECT i.inboundCode FROM Inbound i ORDER BY i.id DESC";
        String lastInboundCode = (String) entityManager.createQuery(query).setMaxResults(1).getResultList().stream().findFirst().orElse(null);

        // 최근 코드가 없으면 첫 번째 코드 "i001"로 시작
        int nextNumber = lastInboundCode == null ? 1 : Integer.parseInt(lastInboundCode.substring(1)) + 1;

        // 3자리 형식으로 코드 생성
        return String.format("%s%03d", prefix, nextNumber);
    }
}
