package pl.javagda25.spring.security.securitytemplate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.javagda25.spring.security.securitytemplate.model.AccountRole;

import java.util.Optional;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    Optional<AccountRole> findByName(String name);

    boolean existsByName(String name);
}
