package com.example.b02.service;

import com.example.b02.dto.CustomerDTO;
import com.example.b02.dto.PageRequestDTO;
import com.example.b02.dto.PageResponseDTO;

public interface CustomerService {

    Long register(CustomerDTO customerDTO);

    CustomerDTO readOne(Long bno);

    void modify(CustomerDTO customerDTO);

    void remove(Long bno);

    PageResponseDTO list(PageRequestDTO pageRequestDTO);
}
