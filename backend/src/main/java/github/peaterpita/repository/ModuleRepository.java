package github.peaterpita.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import github.peaterpita.model.Module;

public interface ModuleRepository extends JpaRepository<Module, String> {
}
