package org.zerock.b02.controller;

import org.springframework.web.bind.annotation.*;
import org.zerock.b02.dto.CustomerDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/mes/customer")
@Log4j2
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO,
                     Model model,
                     @RequestParam(value = "selectedCustomerId", required = false) String selectedCustomerId,
                     @RequestParam(defaultValue = "false") Boolean registerMode,
                     @RequestParam(value = "modifyMode", required = false) Boolean modifyMode,
                     @RequestParam(value = "customerId", required = false) String customerId) {

        // 날짜 범위가 있다면 pageRequestDTO에 설정
        if (pageRequestDTO.getFrom() != null) {
            pageRequestDTO.setFrom(pageRequestDTO.getFrom());
        }
        if (pageRequestDTO.getTo() != null) {
            pageRequestDTO.setTo(pageRequestDTO.getTo());
        }

        // 필터링된 데이터 조회
        PageResponseDTO<CustomerDTO> responseDTO = customerService.list(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);


        // 고객명 목록을 드롭다운에 제공
        List<String> filteredCustomerIds = customerService.getFilteredCustomerIds();
        model.addAttribute("filteredCustomers", filteredCustomerIds);


        if(selectedCustomerId != null){
            CustomerDTO selectedCustomer = customerService.readOne(selectedCustomerId);
            model.addAttribute("selectedCustomer", selectedCustomer);
        }

        model.addAttribute("registerMode", registerMode != null && registerMode);
        model.addAttribute("modifyMode", modifyMode != null && modifyMode);
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
            return "redirect:/mes/customer/list";
        }

        log.info(customerDTO);
        String customerId = customerService.register(customerDTO);
        redirectAttributes.addFlashAttribute("result", customerId);

        return "redirect:/mes/customer/list";
    }

    @GetMapping({"/read", "/modify"})
    public String read(@RequestParam("selectedCustomerId") String selectedCustomerId,
                     PageRequestDTO pageRequestDTO, Model model){

        CustomerDTO customerDTO = customerService.readOne(selectedCustomerId);

        log.info("read:" + customerDTO);

        model.addAttribute("selectedCustomer", customerDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "mes/customer/modify";
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute("customerDTO") CustomerDTO customerDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("customer modify POST" + customerDTO);
        log.info("Received productSize: " + customerDTO.getCustomerSize());

        if(bindingResult.hasErrors()){
            log.info("has errors...");

            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("selectedCustomerId",customerDTO.getCustomerId());
            return "redirect:/mes/customer/list?"+link;
        }

        customerService.modify(customerDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("selectedCustomerId", customerDTO.getCustomerId());

        return "redirect:/mes/customer/list";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam String customerId,
                         RedirectAttributes redirectAttributes){

        log.info("romove post.." + customerId);
        customerService.remove(customerId);
        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/mes/customer/list";
    }
}
