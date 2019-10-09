package pl.javagda25.spring.security.securitytemplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.javagda25.spring.security.securitytemplate.model.AccountRole;
import pl.javagda25.spring.security.securitytemplate.repository.AccountRoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountRoleService {

    private AccountRoleRepository accountRoleRepository;

    @Autowired
    public AccountRoleService(AccountRoleRepository accountRoleRepository) {
        this.accountRoleRepository = accountRoleRepository;
    }

    @Value("${account.default.roles:USER}")
    private String[] defaultRoles;

    public Set<AccountRole> getDefaultRoles() {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String roles : defaultRoles) {
            //znajdz w bazie
            Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(roles);
            //dodaj do kolekcji
            optionalAccountRole.ifPresent(accountRoles::add);
        }
        return accountRoles;
    }

    public List<AccountRole> getAll() {
        return accountRoleRepository.findAll();
    }
}
