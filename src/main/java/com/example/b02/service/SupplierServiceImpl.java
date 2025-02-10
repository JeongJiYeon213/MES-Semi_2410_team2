package com.example.b02.service;

import com.example.b02.domain.Supplier;
import com.example.b02.dto.PageRequestDTO;
import com.example.b02.dto.PageResponseDTO;
import com.example.b02.dto.SupplierDTO;
import com.example.b02.repository.SupplierRepository;
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
public class SupplierServiceImpl implements SupplierService{

    private final ModelMapper modelMapper;

    private final SupplierRepository supplierRepository;

    @Override
    public Long register(SupplierDTO supplierDTO){

        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);

        Long bno = supplierRepository.save(supplier).getBno();

        return bno;
    }

    @Override
    public SupplierDTO readOne(Long bno){

        Optional<Supplier> result = supplierRepository.findById(bno);

        Supplier supplier = result.orElseThrow();

        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return supplierDTO;
    }

    @Override
    public void modify(SupplierDTO supplierDTO){

        Optional<Supplier> result = supplierRepository.findById(supplierDTO.getBno());

        Supplier supplier = result.orElseThrow();

        supplier.change(supplierDTO.getSupplierId(), supplierDTO.getSupplierName(), supplierDTO.getSupplierInfo());

        supplierRepository.save(supplier);
    }

    @Override
    public void remove(Long bno){

        supplierRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Supplier> result = supplierRepository.searchAll(types, keyword, pageable);

        // modelMapper를 통해서 Entity -> DTO로 변환
        List<SupplierDTO> dtoList = result.getContent().stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDTO.class)).collect(Collectors.toList());

        // view엔진에 전달할 정보를 담은 PageResponseDTO 객체 전달
        return PageResponseDTO.<SupplierDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}

