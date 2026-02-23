package com.example.controller;

import com.example.model.LoginHistory;
import com.example.model.User;
import com.example.repository.LoginHistoryRepository;
import com.example.repository.UserRepository;
import com.example.service.GeoLocationService;
import com.example.service.LoginHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final LoginHistoryService historyService;
    private final LoginHistoryRepository historyRepo;
    private final GeoLocationService geoLocationService;

    // ✅ SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {

        if (userRepo.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);

        return ResponseEntity.ok("User registered");
    }

    // ✅ HOME → SAVE LOGIN HISTORY
    @GetMapping("/home")
    public String home(HttpServletRequest request, Principal principal) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        String userAgent = request.getHeader("User-Agent");

        historyService.save(principal.getName(), ip, userAgent);

        return "Welcome " + principal.getName();
    }

    // ✅ GET LOGIN HISTORY
    @GetMapping("/history")
    public List<LoginHistory> history(Principal principal) {
        return historyRepo.findByUsernameOrderByLoginTimeDesc(principal.getName());
    }

    // ✅ LAST LOGIN
    @GetMapping("/last-login")
    public LoginHistory lastLogin(Principal principal) {
        return historyRepo
                .findTopByUsernameOrderByLoginTimeDesc(principal.getName());
    }
    @GetMapping("/test-geo")
    public Map<String, String> test() {
        return geoLocationService.getLocation("8.8.8.8");
    }
}