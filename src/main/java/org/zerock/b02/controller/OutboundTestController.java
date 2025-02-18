package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.domain.Stock;
import org.zerock.b02.dto.OutboundTestDTO;
import org.zerock.b02.dto.StockDTO;
import org.zerock.b02.service.OutboundTestService;
import org.zerock.b02.service.ProductService;
import org.zerock.b02.service.StockService;

import javax.validation.Valid;

@Controller
@RequestMapping("/mes/outbound")
@Log4j2
@RequiredArgsConstructor
public class OutboundTestController {

	private final OutboundTestService outboundTestService;
	private final ProductService productService;
	private final StockService stockService;

	@GetMapping("/list")
	public String listGET(@RequestParam(required = false) Long selectedStockId,
	                      @RequestParam(defaultValue = "false") boolean registerMode,
	                      Model model) {
		model.addAttribute("registerMode", registerMode);
		model.addAttribute("productList", stockService.getAllStocks());

		if(selectedStockId != null) {
			StockDTO selectedStock = stockService.readOne(selectedStockId);
			log.info("Selected: " + selectedStock);
			model.addAttribute("selectedStock", selectedStock);
		}
		return "mes/outbound/list";
	}

	@GetMapping("/register")
	public String registerGET(@RequestParam(required = false) Long selectedStockID, Model model) {

		if(selectedStockID != null) {
			StockDTO selectedStock = stockService.readOne(selectedStockID);
			log.info("selected stock: " + selectedStock);
			log.info("selectedStock.productCode: " + selectedStock.getProductCode());
			model.addAttribute("selectedStock", selectedStock);
		}

		return "mes/outbound/register"; // Thymeleaf 템플릿 경로
	}

	@PostMapping("/register")
	public String registerPost(@Valid OutboundTestDTO outboundTestDTO,
	                           BindingResult bindingResult,
	                           RedirectAttributes redirectAttributes) {
		log.info("Outbound POST register");

		if (bindingResult.hasErrors()) {
			log.info("Validation errors");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/mes/outbound/register";
		}

		String productCode = outboundTestDTO.getProductCode();
		Stock stock = productService.getStockByProductCode(productCode);
		if (stock != null) {
			outboundTestDTO.setStockId(stock.getStockId());
		} else {
			// 재고가 없을 경우 적절한 처리를 할 수 있습니다.
			log.error("Stock not found for product code: " + productCode);
			redirectAttributes.addFlashAttribute("error", "Invalid product code or stock not found");
			return "redirect:/mes/outbound/register";
		}

		log.info(outboundTestDTO);

		Long outboundId = outboundTestService.register(outboundTestDTO);
		redirectAttributes.addFlashAttribute("result", outboundId);

		return "redirect:/mes/stock/list"; // 성공 시 입고 목록 페이지로 리다이렉트
	}

}
