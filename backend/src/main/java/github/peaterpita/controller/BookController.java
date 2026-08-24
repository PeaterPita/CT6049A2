package github.peaterpita.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.peaterpita.dto.BookDto;
import github.peaterpita.model.Book;
import github.peaterpita.service.BookService;
import github.peaterpita.service.CoverService;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final CoverService coverService;

    public BookController(
            BookService bookService,
            CoverService coverService) {
        this.bookService = bookService;
        this.coverService = coverService;
    }

    // ###########################################################
    // # /api/books
    // # Get back all books that fufill search query
    // # and available check
    // ###########################################################
    @GetMapping
    public ResponseEntity<List<BookDto>> searchBooks(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "available", required = false) Boolean available) {

        List<Book> results = bookService.searchBooks(query, available);
        List<BookDto> dtos = results.stream().map(BookDto::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);

    }

    // ###########################################################
    // # /api/books/{bookId}
    // # use bookService to find a book with the passed in ID
    // # if book found return it
    // # else send error
    // ###########################################################
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable String id) {

        try {
            Book book = bookService.findById(id);
            return ResponseEntity.ok(BookDto.toDto(book));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ###########################################################
    // # /api/books/cover/{isbn}
    // # Use custom getCover function on passed in isbn
    // #This function will either return a URI to a cached cover
    // # or a Open Library Retrieved cover
    // ###########################################################
    @GetMapping("/cover/{isbn}")
    public ResponseEntity<byte[]> getCover(@PathVariable String isbn) {

        byte[] image = coverService.getCover(isbn);

        if (image != null && image.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
            return ResponseEntity.status(302)
                    .location(URI.create("/src/assets/placeholder.png"))
                    .build();
        }
    }

}
