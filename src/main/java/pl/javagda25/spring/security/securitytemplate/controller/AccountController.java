package pl.javagda25.spring.security.securitytemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javagda25.spring.security.securitytemplate.model.Account;
import pl.javagda25.spring.security.securitytemplate.services.AccountService;

import javax.validation.Valid;

@Controller
@RequestMapping("/user/")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String registrationFormModel(Model model, Account account) {
        model.addAttribute("newAccount", account);
        return "registration-form";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @Valid Account account,
                           BindingResult result,
                           String passwordConfirm) {
        if (result.hasErrors()) {
            return registrationError(model, account, result.getFieldError().getDefaultMessage());
        }

        //todo: tworzenie konta
        if (!account.getPassword().equals(passwordConfirm)) {
            registrationError(model, account, "Passwords do not match.");

            return "registration-form";
        }
        if (!accountService.register(account)) {
            registrationError(model, account, "User with given username already exists.");

            return "registration-form";
        }

        return "redirect:/login";
    }



    private String registrationError(Model model, Account account, String message) {
        model.addAttribute("newAccount", account);
        model.addAttribute("errorMessage", message);

        return "registration-form";
    }


}
