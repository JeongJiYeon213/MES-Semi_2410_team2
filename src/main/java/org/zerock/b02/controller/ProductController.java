package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.dto.ProductDTO;
import org.zerock.b02.service.ProductService;


import javax.validation.Valid;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes/product")
public class ProductController {
	private final ProductService productService;

	@GetMapping("/main")
	public void main(Model model) {

	}

	@GetMapping("/list")
	public void List(PageRequestDTO pageRequestDTO, Model model){
		PageResponseDTO responseDTO = productService.list(pageRequestDTO);

		log.info(responseDTO);
		model.addAttribute("responseDTO", responseDTO);
	}

	@GetMapping("/register")
	public void RegisterGET() {

	}
	@PostMapping("/register")
	public String RegisterPost(@Valid ProductDTO productDTO,
	                           BindingResult bindingResult,
	                           RedirectAttributes redirectAttributes) {
		log.info("product POST register");

		if (bindingResult.hasErrors()) {
			log.info("error");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

			return "redirect:/mes/product/register";

		}
		log.info(productDTO);

		Long productId = productService.register(productDTO);

		redirectAttributes.addFlashAttribute("result", productId);

		return "redirect:/mes/product/list";
	}

	@GetMapping({"/read", "/modify"})
	public void Read(Long productId, PageRequestDTO pageRequestDTO, Model model) {
		ProductDTO productDTO = productService.readOne(productId);

		log.info(productDTO);

		model.addAttribute("product", productDTO);
	}

	@PostMapping("/modify")
	public String Modify(PageRequestDTO pageRequestDTO,
                         @Valid ProductDTO productDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
		log.info("product modify POST" + productDTO);

		if(bindingResult.hasErrors()) {
			log.info("error");

			String link = pageRequestDTO.getLink();
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			redirectAttributes.addAttribute("productId", productDTO.getProductId());
			return "redirect:/mes/product/modify?"+link;
		}
		productService.modify(productDTO);
		redirectAttributes.addFlashAttribute("result", "modified");
		redirectAttributes.addAttribute("productId", productDTO.getProductId());

		return "redirect:/mes/product/read";
	}

	@PostMapping("/remove")
	public String Remove(Long productId, RedirectAttributes redirectAttributes) {
		log.info("remove POST" + productId);

		productService.remove(productId);

		redirectAttributes.addFlashAttribute("result", "removed");
		return "redirect:/mes/product/list";
	}


	@GetMapping("/searchPopup")
	public String showProductSearchPopup(Model model) {
		List<ProductDTO> productList = productService.getAllProducts();
		model.addAttribute("productList", productList);
		return "/mes/product/searchPopup";
	}
}
