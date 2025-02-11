package com.example.b02.service;

import com.example.b02.domain.Customer;
import com.example.b02.dto.CustomerDTO;
import com.example.b02.dto.PageRequestDTO;
import com.example.b02.dto.PageResponseDTO;
import com.example.b02.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CustomerServiceImpl implements CustomerService{

    private final ModelMapper modelMapper;

    @Autowired
    private final CustomerRepository customerRepository;

    @Override
    public Long register(CustomerDTO customerDTO){

        Customer customer = modelMapper.map(customerDTO, Customer.class);

        Long bno = customerRepository.save(customer).getBno();

        return bno;
    }

    @Override
    public CustomerDTO readOne(Long bno){

        Optional<Customer> result = customerRepository.findById(bno);

        Customer customer = result.orElseThrow();

        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);

        return customerDTO;
    }

    @Override
    public void modify(CustomerDTO customerDTO){

        Optional<Customer> result = customerRepository.findById(customerDTO.getBno());

        Customer customer = result.orElseThrow();

        customer.change(customerDTO.getCustomerId(), customerDTO.getCustomerName(), customerDTO.getCustomerInfo());

        customerRepository.save(customer);
    }

    @Override
    public void remove(Long bno){

        customerRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO){

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Customer> result = customerRepository.searchAll(types, keyword, pageable);

        // modelMapper를 통해서 Entity -> DTO로 변환
        List<CustomerDTO> dtoList = result.getContent().stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class)).collect(Collectors.toList());

        // view엔진에 전달할 정보를 담은 PageResponseDTO 객체 전달
        return PageResponseDTO.<CustomerDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();

        log.info("data" + customers.size());

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<CustomerDTO> getDistinctCustomers() {
        // 중복 제거된 데이터 조회
        List<Customer> distinctCustomers = customerRepository
                .findDistinctByCustomerIdAndCustomerNameAndCustomerInfo();
        return distinctCustomers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }


}