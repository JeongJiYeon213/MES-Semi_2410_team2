package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {

    @Id
    @Size(min = 1, max = 100)
    private String supplierId;

    @NotEmpty
    @Size(min = 1, max = 10)
    @NotBlank(message = "고객명을 입력하세요.")
    private String supplierName;

    @NotEmpty
    @Size(min = 1, max = 8)
    @NotBlank(message = "전화번호를 입력하세요.")
    private String supplierInfo;

    private String supplierSize;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
