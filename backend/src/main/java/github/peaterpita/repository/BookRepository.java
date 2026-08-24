package github.peaterpita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import github.peaterpita.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByCopiesLeftGreaterThan(int copies);
}
