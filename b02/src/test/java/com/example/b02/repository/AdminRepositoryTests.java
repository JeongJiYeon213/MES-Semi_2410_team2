package com.example.b02.repository;

import com.example.b02.domain.Admin;
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
public class AdminRepositoryTests {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1, 100).forEach(i->{
            Admin admin = Admin.builder()
                    .adminId("admin"+i)
                    .password("1234")
                    .adminName("대리"+i)
                    .email(i+"email@naver.com")
                    .department("대리")
                    .position("영업")
                    .build();

            Admin result = adminRepository.save(admin);
            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    public void testSelect(){
        Long bno = 5L;

        Optional<Admin> result = adminRepository.findById(bno);

        Admin admin = result.orElseThrow();

        log.info(admin);
    }

    @Test
    public void testUpdate(){

        Long bno = 3L;

        Optional<Admin> result = adminRepository.findById(bno);

        Admin admin = result.orElseThrow();

        admin.change("hello123","안녕이");

        adminRepository.save(admin);
    }

    @Test
    public void testDelete(){
        Long bno = 1L;

        adminRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Admin> result = adminRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Admin> todoList = result.getContent();

        todoList.forEach(admin -> log.info(admin));
    }
}
