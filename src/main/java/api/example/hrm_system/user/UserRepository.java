package api.example.hrm_system.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByVerifiedTrue();
    List<User> findByVerifiedFalse();
    List<User> findByRole(Role role);

    long countByVerifiedTrue();
    long countByVerifiedFalse();
    long countByRole(Role role);
}