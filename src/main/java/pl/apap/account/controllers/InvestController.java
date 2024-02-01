package pl.apap.account.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apap.account.model.User;
import pl.apap.account.services.ConfirmationEmailService;
import pl.apap.account.services.InvestService;

import java.math.BigDecimal;

@Controller
public class InvestController {

    @Autowired
    InvestService investService;
    @Autowired
    ConfirmationEmailService confirmationEmailService;


    @GetMapping("/invest")
    public String showInvestForm(Model model, HttpSession session){
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

        return "invest";
    }

    @PostMapping("/invest")
    public String invest(Model model, HttpSession session, BigDecimal amount){

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
            model.addAttribute("error", "Amount must be greater than zero for withdraw.");
            return "invest";
        }

        try {
            investService.invest(amount, user);
        } catch (Exception e) {
            model.addAttribute("error", "Error during withdraw: " + e.getMessage());
        }

        return "redirect:/invest/success";
    }

    @GetMapping("/invest/success")
    public String showInvestedSuccessForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("investedMoney", user.getInvestedMoney());

        return "investedSuccess";
    }

    @PostMapping("/invest/success")
    public String investSuccess(HttpSession session){
        User user = (User) session.getAttribute("user");
        String email = (String) user.getEmail();

        String messageBody = "Hi "+user.getName()+" you have successfully invested money. Yours account balace is now "+user.getAccountBalance()+"PLN. Yours invested amount balance is "+user.getInvestedMoney()+"PLN";

        confirmationEmailService.sendConfirmationEmail(email, "Invest Confirmation", messageBody);
        return"redirect:/site";
    }


}
