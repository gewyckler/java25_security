package pl.javagda25.spring.security.securitytemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javagda25.spring.security.securitytemplate.model.Account;
import pl.javagda25.spring.security.securitytemplate.services.AccountRoleService;
import pl.javagda25.spring.security.securitytemplate.services.AccountService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/admin/account/")
@PreAuthorize(value = "hasRole('ADMIN')")
public class AdminAccountController {
    private AccountService accountService;
    private AccountRoleService accountRoleService;

    @Autowired
    public AdminAccountController(AccountService accountService, AccountRoleService accountRoleService) {
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
    }

    @GetMapping("/list")
    public String getUserList(Model model) {
        List<Account> accountList = accountService.findAll();
        model.addAttribute("accounts", accountList);
        return "account-list";
    }

    @GetMapping("/remove")
    public String delete(@RequestParam(name = "accountId") Long accountId) {
        accountService.deleteById(accountId);
        return "redirect:/admin/account/list";
    }

    @GetMapping("/toggleLock")
    public String lock(@RequestParam(name = "accountId") Long accountId) {
        accountService.toggleLock(accountId);
        return "redirect:/admin/account/list";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(Model model,
                                @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (accountOptional.isPresent()) {
            model.addAttribute("account", accountOptional.get());
            return "account-passwordreset";
        }
        return "redirect:/admin/account/list";
    }

    @GetMapping("/editRoles")
    public String editRoles(Model model,
                            @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (accountOptional.isPresent()) {
            model.addAttribute("roles", accountRoleService.getAll());
            model.addAttribute("account", accountOptional.get());
            return "account-roles";
        }
        return "redirect:/admin/account/list";
    }

    @PostMapping("/editRoles")
    public String editRoles(Long accountId, HttpServletRequest request) {
        accountService.editRoles(accountId, request);
        return "redirect:/admin/account/list";
    }
}
