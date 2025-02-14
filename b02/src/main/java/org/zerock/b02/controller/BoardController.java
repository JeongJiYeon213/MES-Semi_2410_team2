package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.BoardDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.BoardService;
import org.zerock.b02.service.LoginService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private LoginService adminService;
    private final BoardService boardService;
    @GetMapping("/adminpage")
    public void adminGet(PageRequestDTO pageRequestDTO, Model model,
                         @RequestParam(value = "selectedAdminBno", required = false) Long selectedAdminBno,
                         @RequestParam(value = "registerMode", required = false) Boolean registerMode,
                         @RequestParam(value = "modifyMode", required = false) Boolean modifyMode
                         ) {


        PageResponseDTO responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);

        if(selectedAdminBno != null) {
            BoardDTO selectedAdmin = boardService.readOne(selectedAdminBno);
            model.addAttribute("selectedAdmin", selectedAdmin);
        }

        model.addAttribute("registerMode", registerMode != null && registerMode);
        model.addAttribute("modifyMode", modifyMode != null && modifyMode);

    }

    @GetMapping("/register")
    public void registerGET() {
        // /board/register.html파일로 이동
    }
    @PostMapping("/register")
    public String registerPOST (
            @Valid BoardDTO boardDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        log.info("POST board register.......");
        log.info(boardDTO);

        // 만약에 todoDTO에서 설정한 validation이 통과되지 않은 경우
        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        Long bno = boardService.register(boardDTO);
        // list.html에 보낼 등록 게시물의 번호 결과값을 전달
        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/adminpage";
    }

    @GetMapping({"/read", "/modify"})
    public String read(@RequestParam("selectedAdminId") Long selectedAdminId,
                       PageRequestDTO pageRequestDTO,
                       Model model) {

        // BoardDTO (관리자) 데이터를 불러옴
        BoardDTO boardDTO = boardService.readOne(selectedAdminId);

        log.info("read: " + boardDTO);

        // 모델에 관리자 데이터를 추가
        model.addAttribute("selectedAdmin", boardDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        // 수정 페이지로 이동
        return "board/modify";  // 이 부분은 수정 폼을 보여주는 view로 변경
    }


    @PostMapping("/modify")
    public String modify(@ModelAttribute("boardDTO") BoardDTO boardDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("board modify POST: " + boardDTO);

        // 유효성 검사에서 오류가 발생한 경우
        if (bindingResult.hasErrors()) {
            log.info("Validation errors occurred");

            String link = pageRequestDTO.getLink();

            // link가 null이거나 비어있으면 기본값 설정 (예: 페이지 1로 이동)
            if (link == null || link.isEmpty()) {
                link = "page=1";  // 기본 페이지 설정
            }

            // 오류 정보를 Flash attribute로 전달
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            // 수정하려는 게시물의 bno를 추가하여 redirect 시 해당 게시물로 이동하도록 설정
            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/adminpage";
        }

        // 게시판 수정 처리
        boardService.modify(boardDTO);

        // 수정 성공 메시지를 Flash attribute로 전달
        redirectAttributes.addFlashAttribute("result", "modified");
        // 수정된 게시물의 bno를 추가하여 리다이렉트
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        // 수정 후 게시판 상세 조회 페이지로 리다이렉트
        return "redirect:/board/adminpage";
    }




    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes rttr) {
        boardService.remove(bno);   // 실제 게시물 번호 삭제

        rttr.addFlashAttribute("result", "removed");

        return "redirect:/board/adminpage";  // 삭제 이후 page번호 1번으로 이동;
    }

    @GetMapping("/login")
    public void loginpage(Model model) {


    }
    // 로그인 요청 처리
    @PostMapping("/login")
    public String login(@RequestParam Long adminId, @RequestParam Long adminPassword) {
        boolean isAuthenticated = adminService.login(adminId, adminPassword);
        if (isAuthenticated) {
            return "redirect:/board/adminpage";
        } else {
            return "redirect:/board/login";
        }

    }
}
