package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer extends BaseEntity {

    @Id
    @Column(length = 50, nullable = false)
    private String customerId;
    @Column(length = 50, nullable = false)
    private String customerName;
    @Column(length = 50, nullable = false)
    private String customerInfo;

    public void change(String customerId, String customerName, String customerInfo){
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerInfo = customerInfo;
    }

}
