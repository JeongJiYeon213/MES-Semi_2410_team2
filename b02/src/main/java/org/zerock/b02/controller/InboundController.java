package org.zerock.b02.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.InboundService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mes")
public class InboundController {
    private final InboundService inboundService;

    @GetMapping("/dashboard")
    public void dashboard(Model model, PageRequestDTO pageRequestDTO){

    }

    // inbound
    @GetMapping("/inbound/list")
    public void list(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = inboundService.list(pageRequestDTO);
        model.addAttribute("responseDTO", pageResponseDTO);
    }

    @GetMapping("/inbound/register")
    public void inboundRegisterGet(){

    }
    @PostMapping("/inbound/register")
    public String registerPost(@Valid InboundDTO inboundDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        log.info("board Post register.........");

        if(bindingResult.hasErrors()){
            log.info("has errors.....");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/inbound/register";
        }

        log.info(inboundDTO);

        Long inboundId = inboundService.register(inboundDTO);
        redirectAttributes.addFlashAttribute("result", inboundId);
        return "redirect:/inbound/list";
    }

    @GetMapping({"/inbound/read", "/inbound/modify"})
    public void read(Long inboundId, PageRequestDTO pageRequestDTO, Model model){
        InboundDTO inboundDTO = inboundService.readOne(inboundId);
        log.info(inboundDTO);
        model.addAttribute("inboundId", inboundDTO);
    }

    @PostMapping("/inbound/modify")
    public String modify(@Valid InboundDTO inboundDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         PageRequestDTO pageRequestDTO){
        log.info("inbound Post modify.........");

        if(bindingResult.hasErrors()){
            log.info("has errors.....");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("inboundId", inboundDTO.getInboundId());
            return "redirect:/inbound/modify"+link;
        }

        inboundService.modify(inboundDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("inboundId", inboundDTO.getInboundId());
        return "redirect:/inbound/read";
    }

    @PostMapping("/remove")
    public String remove(Long inboundId, RedirectAttributes redirectAttributes, PageRequestDTO pageRequestDTO){
        log.info("remove post..." + inboundId);
        String link = pageRequestDTO.getLink();
        inboundService.remove(inboundId);
        redirectAttributes.addFlashAttribute("result", "removed");
        redirectAttributes.addFlashAttribute("link", link);


        return "redirect:/inbound/list";
    }
}
