package com.example.b02.repository;

import com.example.b02.domain.Customer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,5).forEach(i->{
            Customer customer = Customer.builder()
                    .customerId("customerId"+i)
                    .customerName("customerName"+i)
                    .customerInfo("010-1234-567"+i)
                    .build();

            Customer result = customerRepository.save(customer);
            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    public void testSelect(){
        Long bno = 100L;

        Optional<Customer> result = customerRepository.findById(bno);

        Customer customer = result.orElseThrow();

        log.info(customer);

    }

    @Test
    public void testPaging(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Customer> result = customerRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Customer> todoList = result.getContent();

        todoList.forEach(customer -> log.info(customer));
    }
}
