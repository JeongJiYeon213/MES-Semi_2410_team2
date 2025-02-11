package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.InboundRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class InboundServiceImpl implements InboundService{
    private final ModelMapper modelMapper;
    private final InboundRepository inboundRepository;

    @Override
    public Long register(InboundDTO inboundDTO){
        // 마지막 등록된 inboundCode 가져오기
        Optional<Inbound> lastInbound = inboundRepository.findTopByOrderByInboundCodeDesc();

        String newInboundCode = generateNewInboundCode(lastInbound);

        // inboundDTO에 자동 생성된 inboundCode 설정
        inboundDTO.setInboundCode(newInboundCode);

        Inbound inbound = modelMapper.map(inboundDTO, Inbound.class);
        return inboundRepository.save(inbound).getInboundId();
    }

    // inboundCode 생성 로직
    private String generateNewInboundCode(Optional<Inbound> lastInbound) {
        String newInboundCode = "i001";  // 기본값

        if (lastInbound.isPresent()) {
            String lastInboundCode = lastInbound.get().getInboundCode();
            int lastCodeNumber = Integer.parseInt(lastInboundCode.substring(1)); // "i001" -> 1
            newInboundCode = "i" + String.format("%03d", lastCodeNumber + 1); // "i002", "i003", ...
        }

        return newInboundCode;
    }

    @Override
    public InboundDTO readOne(Long inboundId){
        Optional<Inbound> result = inboundRepository.findById(inboundId);
        Inbound inbound = result.orElseThrow(() -> new RuntimeException("Inbound not found"));
        return modelMapper.map(inbound, InboundDTO.class);
    }

    @Override
    public void modify(InboundDTO inboundDTO){
        Optional<Inbound> result = inboundRepository.findById(inboundDTO.getInboundId());
        Inbound inbound = result.orElseThrow();
        inbound.change(inboundDTO.getProductCode(),
                inboundDTO.getInboundCode(),
                inboundDTO.getSupplierId(),
                inboundDTO.getQuantity(),
                inboundDTO.getInboundDate(),
                inboundDTO.getDescription(),
                inboundDTO.getInboundStatus());

        inboundRepository.save(inbound);
    }

    @Override
    public void remove(Long inboundId){
        inboundRepository.deleteById(inboundId);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO){
        // 브라우저에서 요청한 파라미터 값 세팅
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("inboundId");

        // 브라우저에서 받은 파라미터로 board테이블 sql 작성 page<board> 객체로 전달
        Page<Inbound> result = inboundRepository.searchAll(types,keyword, pageable);

        // modelMapper를 통해서 entitu -> dto로 변환
        List<InboundDTO> dtoList = result.getContent().stream()
                .map(inbound -> modelMapper.map(inbound, InboundDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<InboundDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<InboundDTO> getAllInbound() {
        List<Inbound> inbounds = inboundRepository.findAll();  // 전체 데이터 가져오기

        log.info("data" + inbounds.size());

        return inbounds.stream()
                .map(inbound -> modelMapper.map(inbound, InboundDTO.class))
                .collect(Collectors.toList());
    }
}
