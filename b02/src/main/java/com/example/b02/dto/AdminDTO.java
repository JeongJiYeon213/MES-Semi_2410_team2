package com.example.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    private Long bno;

    private String adminId;

    private String password;

    private String adminName;

    private String email;

    private String department;

    private String position;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
