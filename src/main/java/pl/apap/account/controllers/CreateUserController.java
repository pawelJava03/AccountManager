package pl.apap.account.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apap.account.model.User;
import pl.apap.account.repositories.UsersRepository;
import pl.apap.account.services.EmailSenderService;
import pl.apap.account.services.UserService;

@Controller
public class CreateUserController {

    private final UserService userService;
    private final UsersRepository usersRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public CreateUserController(UserService userService, UsersRepository usersRepository, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/user/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create_user";
    }

    @PostMapping("/user/create")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        if (usersRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("message", "Użytkownik o podanym adresie email już istnieje");
            return "create_user";
        } else {
            userService.createUser(user);
            String email = user.getEmail();
            if (user != null && user.getEmail() != null) {
                emailSenderService.SendEmail(user.getEmail(), "Account Manager", "Yours account has been created successfully");
            } else {
                System.out.println("xd");
            }
            System.out.println("user created successfully");
            return "create_user_successfully";
        }
    }
}

