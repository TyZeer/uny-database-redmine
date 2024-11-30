package com.uny.unydatabaseredmine.auth.auth;

import com.uny.unydatabaseredmine.auth.dto.LoginDto;
import com.uny.unydatabaseredmine.auth.payload.request.SignupRequest;
import com.uny.unydatabaseredmine.auth.payload.response.JwtResponse;
import com.uny.unydatabaseredmine.auth.payload.response.MessageResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthMvcController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginRequest") LoginDto loginRequest,
                               HttpSession session,
                               Model model,
                               HttpServletResponse responseCookie) {
        String url = "http://localhost:9090/api/auth/signin";
        try {
            ResponseEntity<JwtResponse> response = restTemplate.postForEntity(url, loginRequest, JwtResponse.class);
            JwtResponse jwtResponse = response.getBody();

            if (jwtResponse != null) {
                setJwtTokenInCookie(responseCookie, jwtResponse.getAccessToken());
                session.setAttribute("Authorization", "Bearer " + jwtResponse.getAccessToken());
                System.out.println("Login successful. Redirecting to /");
                System.out.println("JWT token: Bearer " + jwtResponse.getAccessToken());

                return "redirect:/"; // Redirect to the main page on success
            }
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            System.err.println("Error during login: " + e.getMessage());
        }
        System.out.println("Login failed. Returning to login page.");
        return "login"; // Return to the login page on failure
    }


    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("signupRequest") SignupRequest signupRequest, Model model) {
        String url = "http://localhost:9090/api/auth/signup";
        try {
            ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, signupRequest, MessageResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/auth/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
        }
        return "register";
    }

    public void setJwtTokenInCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("JWT", token);  // "JWT" is the name of the cookie
        cookie.setHttpOnly(true);  // Prevent JavaScript access to the cookie
        cookie.setSecure(true);    // Only sent over HTTPS (for production)
        cookie.setPath("/");       // Ensure the cookie is sent for all paths
        cookie.setMaxAge(3600);    // 1 hour expiration time (adjust as necessary)
        response.addCookie(cookie);
    }
}
