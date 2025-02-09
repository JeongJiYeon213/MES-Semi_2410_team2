package org.zerock.b02.domain;

import javax.persistence.PrePersist;

public class InboundEntityListener {
    @PrePersist
    public void prePersist(Inbound inbound) {
        if (inbound.getInboundCode() == null || inbound.getInboundCode().isEmpty()) {
            // inboundCode가 비어 있으면 자동 생성
            inbound.setInboundCode(generateInboundCode());
        }
    }

    private Object generateInboundCode() {
        String prefix = "i";
        int nextNumber = 1;

        String formattedCode = String.format("%s%03d", prefix, nextNumber);
        return formattedCode;
    }
}
