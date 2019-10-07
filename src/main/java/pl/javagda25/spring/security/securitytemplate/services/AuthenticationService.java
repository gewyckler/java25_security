package pl.javagda25.spring.security.securitytemplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.javagda25.spring.security.securitytemplate.model.Account;
import pl.javagda25.spring.security.securitytemplate.repository.AccountRepository;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByUsername(s);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            return User.builder()
                    .username(account.getUsername())
                    .password(account.getPassword())
                    .roles("USER")
                    .build();
        }

        throw new UsernameNotFoundException("Username not found!!!!");
    }
}
