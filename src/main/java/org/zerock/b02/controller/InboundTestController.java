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
import org.zerock.b02.dto.StockDTO;
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
		return "mes/inbound/list";
	}


	@GetMapping("/register")
	public String registerInbound(@RequestParam(required = false) Long selectedStockId, Model model) {

		if (selectedStockId != null) {
			StockDTO selectedStock = stockService.readOne(selectedStockId);
			log.info("selected stock: " + selectedStock);
			log.info("selectedStock.productCode: " + selectedStock.getProductCode());
			model.addAttribute("selectedStock", selectedStock);
		}
		return "mes/inbound/register";
	}



	@PostMapping("/register")
	public String registerPost(@Valid InboundTestDTO inboundTestDTO,
	                           BindingResult bindingResult,
	                           RedirectAttributes redirectAttributes) {
		log.info("Inbound POST register");


		if (bindingResult.hasErrors()) {
			log.info("Validation errors");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/mes/inbound/register";
		}


		String productCode = inboundTestDTO.getProductCode();
		Stock stock = productService.getStockByProductCode(productCode);
		if (stock != null) {
			inboundTestDTO.setStockId(stock.getStockId());
		} else {
			log.error("Stock not found for product code: " + productCode);
			redirectAttributes.addFlashAttribute("error", "Invalid product code or stock not found");
			return "redirect:/mes/inbound/register";
		}

		log.info(inboundTestDTO);

		Long inboundId = inboundTestService.register(inboundTestDTO);
		redirectAttributes.addFlashAttribute("result", inboundId);

		return "redirect:/mes/stock/list";
	}

}
