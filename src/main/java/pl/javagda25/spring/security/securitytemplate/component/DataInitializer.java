package pl.javagda25.spring.security.securitytemplate.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.javagda25.spring.security.securitytemplate.model.Account;
import pl.javagda25.spring.security.securitytemplate.model.AccountRole;
import pl.javagda25.spring.security.securitytemplate.repository.AccountRepository;
import pl.javagda25.spring.security.securitytemplate.repository.AccountRoleRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private AccountRepository accountRepository;
    private AccountRoleRepository accountRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        addDefaultRole("USER");
        addDefaultRole("ADMIN");

        addDefaultUser("admin", "admin", "ADMIN", "USER");
        addDefaultUser("user", "user", "USER");
    }

    private void addDefaultUser(String username, String password, String... role) {
        if (!accountRepository.existsByUsername(username)) {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(passwordEncoder.encode(password));

            Set<AccountRole> userRoles = findRoles(role);
            account.setAccountRoles(userRoles);

            accountRepository.save(account);
        }
    }

    private void addDefaultRole(String role) {
        if (!accountRoleRepository.existsByName(role)) {
            AccountRole newRole = new AccountRole();
            newRole.setName(role);

            accountRoleRepository.save(newRole);
        }
    }

    private Set<AccountRole> findRoles(String[] roles) {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String role : roles) {
            accountRoleRepository.findByName(role).ifPresent(accountRoles::add);
        }
        return accountRoles;
    }
}
