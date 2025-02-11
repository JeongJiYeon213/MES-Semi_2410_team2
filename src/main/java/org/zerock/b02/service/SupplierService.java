package org.zerock.b02.service;


import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {

    Long register(SupplierDTO supplierDTO);

    SupplierDTO readOne(Long bno);

    void modify(SupplierDTO supplierDTO);

    void remove(Long bno);

    PageResponseDTO list(PageRequestDTO pageRequestDTO);

    List<SupplierDTO> getAllSuppliers();
}
