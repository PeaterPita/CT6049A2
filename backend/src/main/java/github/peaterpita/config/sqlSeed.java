package github.peaterpita.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import github.peaterpita.model.*;
import github.peaterpita.repository.*;

@Configuration
public class sqlSeed {

    // @Bean
    // CommandLineRunner initData(
    // UserRepository userRepo,
    // BookRepository bookRepo,
    // LoanRepository loanRepo,
    // CourseRepository courseRepo,
    // StudentRepository studentRepo,
    // StaffRepository staffRepo) {
    //
    // return args -> {
    // if (bookRepo.findAll().isEmpty()) {
    // seedCourses(courseRepo);
    // seedUsers(userRepo, studentRepo, staffRepo, courseRepo);
    // seedBooks(bookRepo);
    // seedLoans(loanRepo, userRepo, bookRepo);
    // }
    // };
    // }
    //
    // private void seedCourses(CourseRepository courseRepo) {
    // courseRepo.save(makeCourse("BSc Computer Science", "BSC-CS"));
    // courseRepo.save(makeCourse("BSc Cyber Security", "BSC-CYB"));
    // courseRepo.save(makeCourse("BSc Data Science", "BSC-DS"));
    // }
    //
    // private void seedUsers(UserRepository userRepo, StudentRepository
    // studentRepo,
    // StaffRepository staffRepo, CourseRepository courseRepo) {
    // User admin = userRepo.save(makeUser("admin", "admin"));
    // User analyst = userRepo.save(makeUser("analyst", "analyst"));
    // User alice = userRepo.save(makeUser("alice", "password"));
    // User bob = userRepo.save(makeUser("bob", "password"));
    //
    // staffRepo.save(makeStaff(admin.getId(), "STF-001", "ADMIN"));
    // staffRepo.save(makeStaff(analyst.getId(), "STF-002", "DECISION_MAKER"));
    //
    // String csId = courseRepo.findAll().get(0).getCourseId();
    // studentRepo.save(makeStudent(alice.getId(), "STU-001", csId));
    // studentRepo.save(makeStudent(bob.getId(), "STU-002", csId));
    // }
    //
    // private void seedBooks(BookRepository bookRepo) {
    // bookRepo.save(makeBook("9780261103573", "The Lord of the Rings", "J.R.R.
    // Tolkien", 10));
    // bookRepo.save(makeBook("9780134685991", "Effective Java", "Joshua Bloch",
    // 3));
    // bookRepo.save(makeBook("9781501110368", "It Ends With Us", "Colleen Hoover",
    // 5));
    // bookRepo.save(makeBook("9781087939278", "Twisted Love", "Ana Huang", 2));
    // bookRepo.save(makeBook("9780451159274", "It", "Stephen King", 11));
    // bookRepo.save(makeBook("9780007548231", "A Game of Thrones", "George R.R.
    // Martin", 3));
    // bookRepo.save(makeBook("9781407135397", "The Hunger Games", "Suzanne
    // Collins", 7));
    // }
    //
    // private void seedLoans(LoanRepository loanRepo, UserRepository userRepo,
    // BookRepository bookRepo) {
    // String aliceId = userRepo.findByUsername("alice").get().getId();
    // String itEndsId = bookRepo.findByTitleContainingIgnoreCase("It
    // Ends").get(0).getId();
    // String itId = bookRepo.findByTitleContainingIgnoreCase("Stephen
    // King").isEmpty()
    // ? bookRepo.findByTitleContainingIgnoreCase("It").get(0).getId()
    // : bookRepo.findByAuthorContainingIgnoreCase("Stephen King").get(0).getId();
    // String gotId =
    // bookRepo.findByTitleContainingIgnoreCase("Thrones").get(0).getId();
    // String ejId = bookRepo.findByTitleContainingIgnoreCase("Effective
    // Java").get(0).getId();
    // String lotrId = bookRepo.findByTitleContainingIgnoreCase("Lord of the
    // Rings").get(0).getId();
    //
    // loanRepo.save(makeLoan(aliceId, itEndsId, -21, -7, -3, "PAID", 4 * 1.50));
    // loanRepo.save(makeLoan(aliceId, itId, -67, -53, -53, "RETURNED", 0.0));
    // loanRepo.save(makeLoan(aliceId, itId, -53, -39, -33, "PAID", 6 * 1.50));
    // loanRepo.save(makeLoan(aliceId, gotId, -3, 11, null, "BORROWED", 0.0));
    // loanRepo.save(makeLoan(aliceId, ejId, 0, 14, null, "BORROWED", 0.0));
    // loanRepo.save(makeLoan(aliceId, lotrId, -17, -3, null, "BORROWED", 0.0));
    // }
    //
    // // ── helpers ──────────────────────────────────────────────────────────────
    //
    // private User makeUser(String username, String password) {
    // User u = new User();
    // u.setId(UUID.randomUUID().toString());
    // u.setUsername(username);
    // u.setPasswordHash(password);
    // u.setCreatedAt(LocalDateTime.now());
    // return u;
    // }
    //
    // private Student makeStudent(String userId, String studentNumber, String
    // courseId) {
    // Student s = new Student();
    // s.setStudentId(UUID.randomUUID().toString());
    // s.setUserId(userId);
    // s.setStudentNumber(studentNumber);
    // s.setCourseId(courseId);
    // return s;
    // }
    //
    // private Staff makeStaff(String userId, String staffNumber, String role) {
    // Staff s = new Staff();
    // s.setStaffId(UUID.randomUUID().toString());
    // s.setUserId(userId);
    // s.setRole(role);
    // return s;
    // }
    //
    // private Course makeCourse(String name, String code) {
    // Course c = new Course();
    // c.setCourseId(UUID.randomUUID().toString());
    // c.setCourseName(name);
    // c.setCourseCode(code);
    // return c;
    // }
    //
    // private Book makeBook(String isbn, String title, String author, int copies) {
    // Book b = new Book();
    // b.setId(UUID.randomUUID().toString());
    // b.setIsbn(isbn);
    // b.setTitle(title);
    // b.setAuthor(author);
    // b.setCopiesLeft(copies);
    // return b;
    // }
    //
    // private Loan makeLoan(String userId, String bookId,
    // int loanDaysOffset, int dueDaysOffset,
    // Integer returnDaysOffset, String status, double fine) {
    // Loan l = new Loan();
    // l.setId(UUID.randomUUID().toString());
    // l.setUserId(userId);
    // l.setBookId(bookId);
    // l.setLoanDate(LocalDate.now().plusDays(loanDaysOffset));
    // l.setDueDate(LocalDate.now().plusDays(dueDaysOffset));
    // if (returnDaysOffset != null)
    // l.setReturnDate(LocalDate.now().plusDays(returnDaysOffset));
    // l.setStatus(status);
    // l.setFineAmount(fine);
    // return l;
    // }
}
