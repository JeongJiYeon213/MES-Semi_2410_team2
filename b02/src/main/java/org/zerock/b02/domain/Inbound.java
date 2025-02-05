package org.zerock.b02.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
@Builder
@Table(name = "inbound")
public class Inbound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long supplierId;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private LocalDateTime inboundDate;

    @Column(length = 500)
    private String description;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status inboundStatus;

    public enum Status{
        PENDING,
        COMPLETED,
        CANCELED
    }

    // 수정시 변경할것들
    public void change(Long productId,
                       Long supplierId,
                       Long quantity,
                       LocalDateTime inboundDate,
                       String description,
                       Status inboundStatus){
        this.productId = productId;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.inboundDate = inboundDate;
        this.description = description;
        this.inboundStatus = inboundStatus;
    }
}
