package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.domain.Stock;
import org.zerock.b02.dto.InboundTestDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.InboundTestService;
import org.zerock.b02.service.ProductService;
import org.zerock.b02.service.StockService;

import javax.validation.Valid;

@Controller
@RequestMapping("/mes/inbound")
@Log4j2
@RequiredArgsConstructor
public class InboundTestController {

	private final InboundTestService inboundTestService;
	private final ProductService productService;
	private final StockService stockService;

	@GetMapping("/list")
	public String listGET(@RequestParam(required = false, defaultValue = "false") boolean registerMode,
	                    Model model) {
		model.addAttribute("registerMode", registerMode);

		model.addAttribute("productList", stockService.getAllStocks());
		return "mes/inbound/list";
	}

	// GET 요청 처리 - 등록 페이지 보여주기
	@GetMapping("/register")
	public String registerGET(Model model) {
		// 제품 목록을 서비스에서 가져와서 모델에 추가
		model.addAttribute("productList", stockService.getAllStocks());
		return "mes/inbound/register"; // Thymeleaf 템플릿 경로
	}

	// POST 요청 처리 - 등록 처리
	@PostMapping("/register")
	public String registerPost(@Valid InboundTestDTO inboundTestDTO,
	                           BindingResult bindingResult,
	                           RedirectAttributes redirectAttributes) {
		log.info("Inbound POST register");

		// 유효성 검사 오류가 있을 경우
		if (bindingResult.hasErrors()) {
			log.info("Validation errors");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/mes/inbound/register";  // 오류가 있을 경우 다시 등록 페이지로 돌아가기
		}

		// stockId 설정: 제품 코드로 재고 정보 가져오기
		String productCode = inboundTestDTO.getProductCode();
		Stock stock = productService.getStockByProductCode(productCode);  // 해당 서비스에서 재고 정보 가져오기
		if (stock != null) {
			inboundTestDTO.setStockId(stock.getStockId());
		} else {
			// 재고가 없을 경우 적절한 처리를 할 수 있습니다.
			log.error("Stock not found for product code: " + productCode);
			redirectAttributes.addFlashAttribute("error", "Invalid product code or stock not found");
			return "redirect:/mes/inbound/register";  // 재고가 없으면 다시 등록 페이지로 리다이렉트
		}

		log.info(inboundTestDTO);

		// 입고 등록 서비스 호출
		Long inboundId = inboundTestService.register(inboundTestDTO);
		redirectAttributes.addFlashAttribute("result", inboundId);

		return "redirect:/mes/stock/list"; // 성공 시 입고 목록 페이지로 리다이렉트
	}

}
