package github.peaterpita.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import github.peaterpita.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, String> {

    Optional<Staff> findByUserId(String userId);
}
