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
public class Outbound extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboundId;

    @Column(nullable = false, unique = true)
    private String outboundCode;

    @Setter
    @ManyToOne
    @JoinColumn(name = "productCode", referencedColumnName = "productCode", nullable = false)
    private Product product;

    @Setter
    @ManyToOne
    @JoinColumn(name = "supplierId", referencedColumnName = "supplierId", nullable = false)
    private Supplier supplier;

    @Setter
    @Column(nullable = false)
    private Long quantity;

    @Setter
    @Column(nullable = false)
    private LocalDateTime outboundDate;

    @Setter
    @Column(length = 500)
    private String description;

    @Setter
    @Column(name = "status",nullable = false)
    private String outboundStatus;

    // 수정시 변경할것들
    public void change(Product product,
                       String outboundCode,
                       Supplier supplier,
                       Long quantity,
                       LocalDateTime outboundDate,
                       String description,
                       String outboundStatus){
        this.outboundCode = outboundCode;
        this.product = product;
        this.supplier = supplier;
        this.quantity = quantity;
        this.outboundDate = outboundDate;
        this.description = description;
        this.outboundStatus = outboundStatus;
    }
}
