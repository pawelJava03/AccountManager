package pl.apap.account.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;

@Controller
public class LoginController {

    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpSession session) {
        if (usersRepository.existsByEmail(email)) {
            User user = usersRepository.findByEmail(email);
            if (user.getPassword().equals(password)) {
                model.addAttribute("user", user);
                session.setAttribute("user", user);
                return "redirect:/site";
            } else {
                model.addAttribute("error", "Invalid password");
                return "login";
            }
        } else {
            model.addAttribute("error", "Account with this email doesn't exist");
            return "login";
        }
    }
}
