package github.peaterpita.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import github.peaterpita.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
}
