package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Admin;
import org.zerock.b02.dto.AdminDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.AdminRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final ModelMapper modelMapper;
    private final AdminRepository adminRepository;

    @Override
    public Long register(AdminDTO adminDTO) {
        Admin admin = modelMapper.map(adminDTO, Admin.class);

        Long bno = adminRepository.save(admin).getBno();

        return bno;
    }

    @Override
    public AdminDTO readOne(Long bno) {

        Optional<Admin> result = adminRepository.findById(bno);

        Admin admin = result.orElseThrow();

        AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);

        return adminDTO;
    }

    @Override
    public void modify(AdminDTO adminDTO) {
        Optional<Admin> result = adminRepository.findById(adminDTO.getBno());

        Admin admin = result.orElseThrow();

        admin.change(adminDTO.getAdminName(), adminDTO.getAdminId(), adminDTO.getAdminPassword(), adminDTO.getPosition(), adminDTO.getPhoneNum());

        adminRepository.save(admin);
    }

    @Override
    public void remove(Long bno) {
        adminRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO) {
        // 브라우저에서 요청한 파라미터 값 세팅
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        // 브라우저에서 받은 파라미터로 mes테이블 sql 작성하여 Page<Admin> 객체로 전달
        Page<Admin> result = adminRepository.searchAll(types, keyword, pageable);

        // modelMapper를 통해서 Entity -> DTO로 변환
        List<AdminDTO> dtoList = result.getContent().stream()
                .map(admin -> modelMapper.map(admin, AdminDTO.class)).collect(Collectors.toList());

        // view엔진에 전달할 정보를 담은 PageResponseDTO 객체 전달
        return PageResponseDTO.<AdminDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        List<Admin> products = adminRepository.findAll();  // 전체 데이터 가져오기

        log.info("data" + products.size());

        return products.stream()
                .map(product -> modelMapper.map(product, AdminDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Admin login(Long adminId, int adminPassword) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminId(adminId);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            // 비밀번호가 일치하는지 확인
            if (admin.getAdminPassword() == adminPassword) {
                return admin; // 로그인 성공: Admin 객체 반환
            }
        }
        return null; // 로그인 실패: null 반환
    }

}
