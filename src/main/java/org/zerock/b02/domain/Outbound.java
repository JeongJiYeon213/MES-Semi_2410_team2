package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
@Builder
@Table(name = "outbound")
public class Outbound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboundId;

    @Column(nullable = false)
    private String outboundCode;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String supplierId;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private LocalDateTime outboundDate;

    @Column(length = 500)
    private String description;

    @Column(name = "status",nullable = false)
    private String outboundStatus;

    // 수정시 변경할것들
    public void change(String productCode,
                       String outboundCode,
                       String supplierId,
                       Long quantity,
                       LocalDateTime outboundDate,
                       String description,
                       String outboundStatus){
        this.outboundCode = outboundCode;
        this.productCode = productCode;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.outboundDate = outboundDate;
        this.description = description;
        this.outboundStatus = outboundStatus;
    }
}
