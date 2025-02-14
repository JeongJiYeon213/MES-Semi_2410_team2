package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Admin;
import org.zerock.b02.dto.BoardDTO;
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
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;
    private final AdminRepository adminRepository; // ✅ 필드명 수정

    @Override
    public Long register(BoardDTO boardDTO) {
        Admin admin = modelMapper.map(boardDTO, Admin.class);

        Long bno = adminRepository.save(admin).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {

        Optional<Admin> result = adminRepository.findById(bno);

        Admin admin = result.orElseThrow();

        BoardDTO boardDTO = modelMapper.map(admin, BoardDTO.class);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Admin> result = adminRepository.findById(boardDTO.getBno());

        Admin admin = result.orElseThrow();

        admin.change(boardDTO.getAdminName(), boardDTO.getAdminId(), boardDTO.getAdminPassword(), boardDTO.getPosition(), boardDTO.getPhoneNum());

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

        // 브라우저에서 받은 파라미터로 board테이블 sql 작성하여 Page<Board> 객체로 전달
        Page<Admin> result = adminRepository.searchAll(types, keyword, pageable);

        // modelMapper를 통해서 Entity -> DTO로 변환
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());

        // view엔진에 전달할 정보를 담은 PageResponseDTO 객체 전달
        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
