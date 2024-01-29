package pl.apap.account.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apap.account.model.User;
import pl.apap.account.services.DepositService;

import java.math.BigDecimal;

@Controller
public class DepositController {

    @Autowired
    DepositService depositService;

    @GetMapping("/deposit")
    public String showDepositForm(HttpSession session, Model model, BigDecimal amount){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", user.getName());
        model.addAttribute("accBalance", user.getAccountBalance());
        model.addAttribute("totalEarned", user.getTotalEarned());
        model.addAttribute("totalSpent", user.getTotalSpent());
        model.addAttribute("amount", amount);


        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(HttpSession session, BigDecimal amount, Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", user.getName());
        model.addAttribute("accBalance", user.getAccountBalance());
        model.addAttribute("totalEarned", user.getTotalEarned());
        model.addAttribute("totalSpent", user.getTotalSpent());
        model.addAttribute("amount", amount);


        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "Amount must be greater than zero for deposit.");
            return "deposit";
        }
        try {
            depositService.deposit(amount);
        } catch (Exception e) {
            model.addAttribute("error", "Error during deposit: " + e.getMessage());
        }

        return "deposit";
    }

}
