package github.peaterpita.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.peaterpita.dto.LoanDto;
import github.peaterpita.model.Loan;
import github.peaterpita.model.User;
import github.peaterpita.repository.UserRepository;
import github.peaterpita.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;
    private final UserRepository userRepo;

    public LoanController(
            LoanService loanService,
            UserRepository userRepository) {
        this.loanService = loanService;
        this.userRepo = userRepository;
    }

    // ###########################################################
    // # Helper function to get currently signed in user through
    // # their JWT token.
    // # JWT token can be reversed to get the users username.
    // # This username can then be used
    // # to get overall user object
    // ###########################################################
    private User getCurrentUser(Authentication auth) {
        String username = (String) auth.getPrincipal();
        return userRepo.findByUsername(username).orElseThrow();
    }

    // ###########################################################
    // # Return back all loans associated with `me` (currently
    // # logged in user)
    // ###########################################################
    @GetMapping("/me")
    public ResponseEntity<List<LoanDto>> myLoans(Authentication auth) {
        User user = getCurrentUser(auth);
        List<LoanDto> loans = loanService.findLoansByUser(user.getId());
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrow(
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        User user = getCurrentUser(auth);
        String bookId = ((String) body.get("bookId")).toString();
        try {
            Loan loan = loanService.borrowBook(user.getId(), bookId);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException err) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", err.getMessage()));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBook(
            @RequestBody Map<String, String> body, Authentication auth) {
        User user = getCurrentUser(auth);

        String loanId = (body.get("loanId"));
        try {
            Loan loan = loanService.returnBook(user.getId(), loanId);
            return ResponseEntity.ok(loan);
        } catch (IllegalStateException err) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", err.getMessage()));
        }
    }

    @PostMapping("/pay/{loanId}")
    public ResponseEntity<?> payFine(
            @PathVariable String loanId,
            Authentication auth) {
        User user = getCurrentUser(auth);

        try {
            loanService.payFine(user.getId(), loanId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Paid"));
        } catch (RuntimeException err) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", err.getMessage()));
        }
    }

    // ###########################################################
    // # /api/loans/history
    // # Two paramets passed in through headers. A beginning
    // # date and an end date
    // # This endpoint then returns all loans that occurred
    // # between those dates
    // ###########################################################
    @GetMapping("/history")
    public ResponseEntity<List<LoanDto>> getHistroy(
            @RequestParam LocalDate beginDate,
            @RequestParam LocalDate endDate,
            Authentication auth) {
        User user = getCurrentUser(auth);
        List<LoanDto> history = loanService.getLoanHistory(
                user.getId(),
                beginDate,
                endDate);
        return ResponseEntity.ok(history);
    }

}
