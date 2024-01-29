package pl.apap.account.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;

@Controller
public class UserInfoController {

    private final UsersRepository usersRepository;

    @Autowired
    public UserInfoController(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @GetMapping("/user-info/{email}")
    public String hello(@PathVariable String email, Model model) {
        User user = usersRepository.findByEmail(email);
        model.addAttribute("user", user);

        return "userInfo";
    }
}