package pl.apap.account.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.apap.account.model.User;
import pl.apap.account.services.ConfirmationEmailService;
import pl.apap.account.services.DepositService;


import java.math.BigDecimal;

@Controller
public class DepositController {

    private final DepositService depositService;

    @Autowired
    ConfirmationEmailService emailService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping("/deposit")
    public String showDepositForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("investedMoney", user.getInvestedMoney());
        model.addAttribute("userName", user.getName());
        model.addAttribute("accBalance", user.getAccountBalance());
        model.addAttribute("totalEarned", user.getTotalEarned());
        model.addAttribute("totalSpent", user.getTotalSpent());
        model.addAttribute("amount", BigDecimal.ZERO);

        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(HttpSession session, BigDecimal amount, RedirectAttributes redirectAttributes, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("investedMoney", user.getInvestedMoney());
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
            depositService.deposit(user, amount);
            redirectAttributes.addFlashAttribute("userName", user.getName());
            redirectAttributes.addFlashAttribute("accBalance", user.getAccountBalance());
            redirectAttributes.addFlashAttribute("amount", amount);
        } catch (Exception e) {
            model.addAttribute("error", "Error during deposit: " + e.getMessage());
        }

        return "redirect:/deposit/success";
    }



    @GetMapping("/deposit/success")
    public String showDepositSuccessForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }


        model.addAttribute("accBalance", user.getAccountBalance());


        return "depositSuccess";
    }
    @PostMapping("/deposit/success")
    public String depositSuccess(HttpSession session){
        User user = (User) session.getAttribute("user");
        String email = (String) user.getEmail();

        String messageBody = "Hi "+user.getName()+" you have successfully deposited money from yours account. Yours account balace is now "+user.getAccountBalance()+"PLN";

        emailService.sendConfirmationEmail(email, "Deposit Confirmation", messageBody);
        return"redirect:/site";
    }



}
