package pl.apap.account.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apap.account.model.User;
import pl.apap.account.services.WithdrawService;

import java.math.BigDecimal;

@Controller
public class WithdrawController {

    @Autowired
    WithdrawService withdrawService;



    @GetMapping("/withdraw")
    public String showWithdrawForm(HttpSession session, BigDecimal amount, Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", user.getName());
        model.addAttribute("accBalance", user.getAccountBalance());
        model.addAttribute("totalEarned", user.getTotalEarned());
        model.addAttribute("totalSpent", user.getTotalSpent());
        model.addAttribute("amount", BigDecimal.ZERO);

        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(HttpSession session, BigDecimal amount, Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", user.getName());
        model.addAttribute("accBalance", user.getAccountBalance());
        model.addAttribute("totalEarned", user.getTotalEarned());
        model.addAttribute("totalSpent", user.getTotalSpent());
        model.addAttribute("amount", BigDecimal.ZERO);


        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "Amount must be greater than zero for withdraw.");
            return "withdraw";
        }

        try {
            withdrawService.withdraw(amount, user);
        } catch (Exception e) {
            model.addAttribute("error", "Error during withdraw: " + e.getMessage());
        }

        return "redirect:/withdraw/success";
    }


    @GetMapping("/withdraw/success")
    public String showDepositSuccessForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }


        model.addAttribute("accBalance", user.getAccountBalance());


        return "withdrawSuccess";
    }




}