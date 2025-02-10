package com.example.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String supplierId;

    @NotEmpty
    private String supplierName;

    @NotEmpty
    private String supplierInfo;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
