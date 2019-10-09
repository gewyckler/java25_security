package pl.javagda25.spring.security.securitytemplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.javagda25.spring.security.securitytemplate.model.Account;
import pl.javagda25.spring.security.securitytemplate.model.AccountRole;
import pl.javagda25.spring.security.securitytemplate.repository.AccountRepository;
import pl.javagda25.spring.security.securitytemplate.repository.AccountRoleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRoleService accountRoleService;
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountRoleService accountRoleService, AccountRoleRepository accountRoleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRoleService = accountRoleService;
        this.accountRoleRepository = accountRoleRepository;
    }

    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }
        //szyfrowanie hasła
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getDefaultRoles());

        //zapis do bazy
        accountRepository.save(account);
        return true;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public void deleteById(Long userId) {
        if (accountRepository.existsById(userId)) {
            Account account = accountRepository.getOne(userId);
            if (!account.isAdmin()) {
                accountRepository.deleteById(userId);
            }
        }
    }

    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public void update(Account account) {
        accountRepository.save(account);
    }

    public void toggleLock(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);
            if (!account.isAdmin()) {
                if (!account.isLocked()) {
                    account.setLocked(!account.isLocked());
                    accountRepository.save(account);
                }
            }
        }
    }

    public void editRoles(Long accountId, HttpServletRequest request) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);

            Map<String, String[]> formParameters = request.getParameterMap();
            Set<AccountRole> newCollectionOfRoles = new HashSet<>();

            for (String roleName : formParameters.keySet()) {
                String[] values = formParameters.get(roleName);

                if (values[0].equals("on")) {
                    Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByName(roleName);

                    if (accountRoleOptional.isPresent()) {
                        newCollectionOfRoles.add(accountRoleOptional.get());
                    }
                }
            }
            account.setAccountRoles(newCollectionOfRoles);
            accountRepository.save(account);
        }
    }
}
