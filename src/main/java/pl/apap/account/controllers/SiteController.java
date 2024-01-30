package pl.apap.account.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.apap.account.model.User;

@Controller
@RequestMapping("/site")
public class SiteController {

    @GetMapping()
    public String showSiteForm(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user != null){
            model.addAttribute("investedMoney", user.getInvestedMoney());
            model.addAttribute("userName", user.getName());
            model.addAttribute("accBalance", user.getAccountBalance());
            model.addAttribute("totalEarned", user.getTotalEarned());
            model.addAttribute("totalSpent", user.getTotalSpent());
            return "site";
        }else return "redirect:/login";
    }




}
