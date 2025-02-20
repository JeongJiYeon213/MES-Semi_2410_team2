package org.zerock.b02.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Supplier extends BaseEntity implements Serializable {

    @Id
    @Column(length = 50, nullable = false)
    private String supplierId;
    @Column(length = 50, nullable = false)
    private String supplierName;
    @Column(length = 50, nullable = false)
    private String supplierInfo;

    private static final AtomicInteger customerIdCounter = new AtomicInteger(1);

    @PrePersist
    public synchronized void prePersist() {
        if (this.supplierId == null) {
            this.supplierId = generateSupplierId();
        }
    }

    private String generateSupplierId() {
        return "company" + generateRandomAlphaNumeric(4);
    }

    private String generateRandomAlphaNumeric(int length) {
        String characters = "ABCDEFGH0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public void change(String supplierId, String supplierName, String supplierInfo) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierInfo = supplierInfo;
    }

}