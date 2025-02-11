package com.example.b02.controller;

import com.example.b02.dto.CustomerDTO;
import com.example.b02.dto.PageRequestDTO;
import com.example.b02.dto.PageResponseDTO;
import com.example.b02.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/customer")
@Log4j2
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/main")
    public void mainGET(){

    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model,
                     @RequestParam(value = "selectedBno", required = false) Long selectedBno){

        PageResponseDTO<CustomerDTO> responseDTO = customerService.list(pageRequestDTO);

        log.info(responseDTO);

        List<CustomerDTO> distinctCustomers = customerService.getDistinctCustomers();

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("distinctCustomers", distinctCustomers);

        if(selectedBno != null){
            CustomerDTO selectedCustomer = customerService.readOne(selectedBno);
            model.addAttribute("selectedCustomer", selectedCustomer);
        }

    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid CustomerDTO customerDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("customer POST register...");

        if (bindingResult.hasErrors()) {
            log.info("has errors...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/customer/register";
        }

        log.info(customerDTO);

        Long bno = customerService.register(customerDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/customer/list";
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid CustomerDTO customerDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("customer modify post..." + customerDTO);

        if(bindingResult.hasErrors()){
            log.info("has errors...");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno",customerDTO.getBno());

            return "redirect:/customer/modify?"+link;
        }

        customerService.modify(customerDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", customerDTO.getBno());

        return "redirect:/customer/read";
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes){

        log.info("romove post.." + bno);

        customerService.remove(bno);

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/customer/list";
    }

}
