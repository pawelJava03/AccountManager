package pl.apap.account.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apap.account.model.User;
import pl.apap.account.services.ConfirmationEmailService;
import pl.apap.account.services.WithdrawInvestmentService;

import java.math.BigDecimal;

@Controller
public class WithdrawInvestmentController {


    @Autowired
    WithdrawInvestmentService withdrawInvestmentService;
    @Autowired
    ConfirmationEmailService emailService;

    @GetMapping("/withdraw-investment")
    public String showWithdrawInvestmentForm(HttpSession session, Model model){
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

        return "withdraw_investment";
    }

    @PostMapping("/withdraw-investment")
    public String WithdrawInvestment(HttpSession session, Model model, BigDecimal amount){
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

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "Amount must be greater than zero to withdraw.");
            return "withdraw_investment";
        }

        try {
            withdrawInvestmentService.withdrawInvestment(amount, user);
        } catch (Exception e) {
            model.addAttribute("error", "Error during withdraw: " + e.getMessage());
        }

        return "redirect:/withdraw-investment/success";
    }


    @GetMapping("/withdraw-investment/success")
    public String showDepositSuccessForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }


        model.addAttribute("accBalance", user.getAccountBalance());
        model.addAttribute("investedBalance", user.getInvestedMoney());


        return "withdraw_investment_success";
    }

    @PostMapping("/withdraw-investment/success")
    public String depositSuccess(HttpSession session){
        User user = (User) session.getAttribute("user");
        String email = (String) user.getEmail();

        String messageBody = "Hi "+user.getName()+" you have successfully withdrawn invested money from your account. Yours account balace is now "+user.getAccountBalance()+"PLN, yours invested money balance is now " + user.getInvestedMoney()+"PLN";

        emailService.sendConfirmationEmail(email, "Withdraw Investment Confirmation", messageBody);
        return"redirect:/site";
    }}
