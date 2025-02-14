package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Admin;
import org.zerock.b02.repository.AdminRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    @Autowired
    private AdminRepository adminRepository;

    // 로그인 처리
    public boolean login(Long adminId, Long adminPassword) {
        // Admin ID로 Admin을 찾음
        Optional<Admin> adminOpt = adminRepository.findByAdminId(adminId);

        // Admin이 존재하고, 비밀번호가 일치하는지 확인
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return admin.getAdminPassword() == adminPassword; // 비밀번호 비교
        }

        return false;
    }


}
