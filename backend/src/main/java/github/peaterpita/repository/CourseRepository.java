package github.peaterpita.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import github.peaterpita.model.Course;

public interface CourseRepository extends JpaRepository<Course, String> {
}
