
package github.peaterpita.dto;

import java.time.LocalDate;

public class LoanDto {
    public String id;
    public BookDto book;
    public LocalDate loanDate;
    public LocalDate dueDate;
    public LocalDate returnDate;
    public String status;
    public Double fine;
}
