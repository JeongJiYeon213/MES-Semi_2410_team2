package com.example.b02.controller;

import com.example.b02.dto.CustomerDTO;
import com.example.b02.dto.PageRequestDTO;
import com.example.b02.dto.PageResponseDTO;
import com.example.b02.dto.SupplierDTO;
import com.example.b02.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/supplier")
@Log4j2
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        PageResponseDTO responseDTO = supplierService.list(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);

    }
    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid SupplierDTO supplierDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("supplier POST register...");

        if (bindingResult.hasErrors()) {
            log.info("has errors...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/supplier/register";
        }

        log.info(supplierDTO);

        Long bno = supplierService.register(supplierDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/supplier/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        SupplierDTO supplierDTO = supplierService.readOne(bno);

        log.info(supplierDTO);

        model.addAttribute("dto",supplierDTO);
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid SupplierDTO supplierDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("supplier modify post..." + supplierDTO);

        if(bindingResult.hasErrors()){
            log.info("has errors...");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno",supplierDTO.getBno());

            return "redirect:/supplier/modify?"+link;
        }

        supplierService.modify(supplierDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", supplierDTO.getBno());

        return "redirect:/supplier/read";
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes){

        log.info("romove post.." + bno);

        supplierService.remove(bno);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/supplier/list";
    }
}
