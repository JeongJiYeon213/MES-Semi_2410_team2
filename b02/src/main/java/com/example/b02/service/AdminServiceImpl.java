package com.example.b02.service;

import com.example.b02.domain.Admin;
import com.example.b02.dto.AdminDTO;
import com.example.b02.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService{

    private final ModelMapper modelMapper;

    private final AdminRepository adminRepository;

    @Override
    public Long register(AdminDTO adminDTO){

        Admin admin = modelMapper.map(adminDTO, Admin.class);

        Long bno = adminRepository.save(admin).getBno();

        return bno;
    }
}
