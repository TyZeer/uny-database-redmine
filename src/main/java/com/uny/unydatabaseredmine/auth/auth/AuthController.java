package com.uny.unydatabaseredmine.auth.auth;

import com.uny.unydatabaseredmine.auth.dto.BearerToken;
import com.uny.unydatabaseredmine.auth.dto.LoginDto;
import com.uny.unydatabaseredmine.auth.dto.RegisterDto;
import com.uny.unydatabaseredmine.auth.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService iUserService;
    @GetMapping("/register")
    public String getRegisterView(@ModelAttribute("registerDto") RegisterDto registerDto){
        return "registration";
    }

    @PostMapping("/register")
    public ModelAndView registrationResult(@ModelAttribute RegisterDto registerDto){
        try{
            registerDto.setUserRole("USER");
            var response = iUserService.register(registerDto);
            ResponseEntity<BearerToken> responseEntity = (ResponseEntity<BearerToken>) response;
            // Get the Bearer token from the ResponseEntity
            String bearerTokenValue = Objects.requireNonNull(responseEntity.getBody()).getAccessToken();

            // Set the Bearer token in the response header
            HttpHeaders headers = new HttpHeaders();
            String token = "Bearer " + bearerTokenValue;
            headers.set("Authorization", token);
            ModelAndView modelAndView = new ModelAndView("redirect:login");
            modelAndView.addAllObjects(headers);
            return modelAndView;
        }catch (Exception e){
            ModelAndView modelAndView = new ModelAndView("registration");
            modelAndView.addObject("error","Ошибка регистрации");
            return modelAndView;
        }
    }

    @GetMapping("/login")
    public String getLoginView(@ModelAttribute("loginDto") LoginDto loginDto){
        return "login";
    }
    @PostMapping("/login")
    public ModelAndView loginResult(@ModelAttribute LoginDto loginDto) {
        try {
            var response = iUserService.authenticate(loginDto);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", response);
            ModelAndView modelAndView = new ModelAndView("redirect:index");
            modelAndView.addAllObjects(headers);
            return modelAndView;
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "Ошибка регистрации");
            return modelAndView;
        }
    }
}
