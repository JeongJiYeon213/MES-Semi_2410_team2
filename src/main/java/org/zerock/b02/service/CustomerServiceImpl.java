package org.zerock.b02.service;

import org.zerock.b02.domain.Customer;
import org.zerock.b02.dto.CustomerDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    @Override
    public String register(CustomerDTO customerDTO) {

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        String customerId = customerRepository.save(customer).getCustomerId();
        return customerId;
    }

    @Override
    public CustomerDTO readOne(String customerId) {

        Optional<Customer> result = customerRepository.findById(customerId);
        Customer customer = result.orElseThrow();
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }

    @Override
    @Transactional
    public void modify(CustomerDTO customerDTO) {

        Optional<Customer> result = customerRepository.findById(customerDTO.getCustomerId());
        Customer customer = result.orElseThrow();
        customer.change(customerDTO.getCustomerId(),
                customerDTO.getCustomerName(),
                customerDTO.getCustomerInfo());
        customerRepository.save(customer);
    }

    @Override
    public void remove(String customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public PageResponseDTO<CustomerDTO> list(PageRequestDTO pageRequestDTO) {

        // 날짜 범위 및 고객 필터링 정보를 가져옴
        String customerId = pageRequestDTO.getCustomerId();

        // 날짜 범위 및 고객 필터링 정보를 pageRequestDTO에 설정
        if (customerId != null && !customerId.isEmpty()) {
            pageRequestDTO.setCustomerId(customerId);
        }

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("customerId");

        // 날짜 범위나 고객 ID가 주어졌을 경우, 필터링된 검색
        if (customerId != null && !customerId.isEmpty()) {
            Page<Customer> result = customerRepository.searchWithFilters(keyword, customerId, pageable);
            List<CustomerDTO> dtoList = result.getContent().stream()
                    .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                    .collect(Collectors.toList());

            return PageResponseDTO.<CustomerDTO>withAll()
                    .pageRequestDTO(pageRequestDTO)
                    .dtoList(dtoList)
                    .total((int) result.getTotalElements())
                    .build();
        }

        // 기본 검색 (필터링 조건이 없을 경우)
        Page<Customer> result = customerRepository.searchAll(types, keyword, pageable);
        List<CustomerDTO> dtoList = result.getContent().stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());


        return PageResponseDTO.<CustomerDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        log.info("data" + customers.size());

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    // 드롭다운 렌더링
    @Override
    public List<String> getFilteredCustomerIds() {
        // 전체 고객 목록을 가져와서 customerId만 추출하고 중복을 제거하여 반환
        List<String> customerIds = customerRepository.findAll().stream()
                .map(Customer::getCustomerId) // id만 추출
                .distinct() // 중복 제거
                .collect(Collectors.toList()); // 리스트로 변환
        return customerIds;
    }


}