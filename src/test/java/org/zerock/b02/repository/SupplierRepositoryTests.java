package org.zerock.b02.repository;

import org.zerock.b02.domain.Supplier;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class SupplierRepositoryTests {

    @Autowired
    public SupplierRepository supplierRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,5).forEach(i->{
            Supplier supplier = Supplier.builder()
                    .supplierId("b"+i)
                    .supplierName("철강"+i)
                    .supplierInfo("0111234567"+i)
                    .build();

            Supplier result = supplierRepository.save(supplier);
            log.info("SupplierId: " + result.getSupplierId());
        });
    }
}
