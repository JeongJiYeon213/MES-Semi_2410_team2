package com.example.b02.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(unique = true, length = 50, nullable = false)
    private String adminId;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String adminName;

    private String email;

    private String department;

    private String position;

    public void change(String adminId, String adminName){
        this.adminId = adminId;
        this.adminName = adminName;
    }
}
