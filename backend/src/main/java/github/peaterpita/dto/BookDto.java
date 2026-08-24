
package github.peaterpita.dto;

import github.peaterpita.model.Book;

public class BookDto {
    public String id;
    public String isbn;
    public String title;
    public String author;
    public String coverURL;
    public Integer copiesLeft;

    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();

        dto.id = book.getId();
        dto.isbn = book.getIsbn();
        dto.title = book.getTitle();
        dto.author = book.getAuthor();
        dto.copiesLeft = book.getCopiesLeft();

        // #######################################################
        // # If no ISBN present (never should occur)
        // # set coverURL to a placeholder image stored on the
        // # frontend
        // #######################################################
        if (book.getIsbn() != null) {
            dto.coverURL = "/api/books/cover/" + book.getIsbn();
        } else {
            dto.coverURL = "/assets/placeholder.png";
        }

        return dto;
    }

}
