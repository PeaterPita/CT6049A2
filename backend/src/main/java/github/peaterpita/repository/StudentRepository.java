package github.peaterpita.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import github.peaterpita.model.Student;

public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByUserId(String userId);
}
