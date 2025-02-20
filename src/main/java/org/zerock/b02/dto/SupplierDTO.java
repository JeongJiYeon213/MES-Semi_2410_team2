package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.b02.domain.Supplier;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {

    @Id
    @Size(min = 1, max = 10)
    private String supplierId;

    @NotEmpty(message = "고객명을 입력하세요.")
    private String supplierName;

    @NotEmpty(message = "전화번호를 입력하세요.")
    private String supplierInfo;

    private String supplierSize;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public Supplier toEntity() {
        return Supplier.builder()
                .supplierName(this.supplierName)
                .supplierInfo(this.supplierInfo)
                .build();
    }
}
