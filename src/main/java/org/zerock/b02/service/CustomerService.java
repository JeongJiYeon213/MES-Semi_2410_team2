package org.zerock.b02.service;

import org.zerock.b02.domain.Customer;
import org.zerock.b02.dto.CustomerDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {

    String register(CustomerDTO customerDTO);

    CustomerDTO readOne(String customerId);

    void modify(CustomerDTO customerDTO);

    void remove(String customerId);

    PageResponseDTO list(PageRequestDTO pageRequestDTO);

    List<CustomerDTO> getAllCustomers();

    // 드롭다운 렌더링
    List<String> getFilteredCustomerIds();

}
