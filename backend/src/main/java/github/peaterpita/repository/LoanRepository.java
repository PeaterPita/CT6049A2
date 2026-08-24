package github.peaterpita.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import github.peaterpita.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, String> {

    List<Loan> findByUserId(String userId);

    @Query("SELECT l FROM Loan l WHERE l.userId = :userId " +
           "AND l.loanDate BETWEEN :begin AND :end")
    List<Loan> findHistoryBetween(
            @Param("userId") String userId,
            @Param("begin") LocalDate begin,
            @Param("end") LocalDate end);

    boolean existsByUserIdAndBookIdAndReturnDateIsNull(String userId, String bookId);
}
