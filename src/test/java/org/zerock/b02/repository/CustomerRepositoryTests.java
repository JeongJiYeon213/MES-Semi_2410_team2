package org.zerock.b02.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b02.domain.Customer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testInsertCustomer() {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            Customer customer = Customer.builder()
                    .customerName("고객" + i)
                    .customerInfo("010" + i)
                    .build();

            Customer result = customerRepository.save(customer);

            log.info("Saved Customer: " + result);
        });
    }

    @Test
    public void testFindById() {

    }



}