package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "login_history")
@Getter
@Setter
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String ipAddress;

    private String browser;

    private String os;

    private String deviceType;

    private String deviceId;   // ⭐ important

    private String city;

    private String country;

    private LocalDateTime loginTime;

    private boolean currentSession;  // ⭐ highlight current device
}