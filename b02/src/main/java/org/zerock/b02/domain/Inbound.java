package org.zerock.b02.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.zerock.b02.dto.InboundDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
@Builder
@Table(name = "inbound")
@EntityListeners(InboundEntityListener.class)
public class Inbound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundId;

    @Column(nullable = false)
    private String inboundCode;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String supplierId;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private LocalDateTime inboundDate;

    @Column(length = 500)
    private String description;

    @Column(name = "status",nullable = false)
    private String inboundStatus;

    // 수정시 변경할것들
    public void change(String productCode,
                       String inboundCode,
                       String supplierId,
                       Long quantity,
                       LocalDateTime inboundDate,
                       String description,
                       String inboundStatus){
        this.inboundCode = inboundCode;
        this.productCode = productCode;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.inboundDate = inboundDate;
        this.description = description;
        this.inboundStatus = inboundStatus;
    }
}
