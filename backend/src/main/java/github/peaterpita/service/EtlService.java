package github.peaterpita.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class EtlService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Map<String, Integer> run() {
        loadDimDate();
        Map<String, Integer> result = new HashMap<>();
        result.put("courses", loadDimCourse());
        result.put("books", loadDimBook());
        result.put("students", loadDimStudent());
        result.put("loans", loadFactLoans());

        return result;
    }

    private int loadDimDate() {
        String query = """
                    INSERT INTO warehouse.dim_date (full_date, day_of_week, day_name, week_of_year, month, month_name, year, is_weekend)
                    SELECT DISTINCT
                        loan_date,
                        EXTRACT(DOW FROM loan_date)::INT,
                        TO_CHAR(loan_date, 'FMDay'),
                        EXTRACT(WEEK FROM loan_date)::INT,
                        EXTRACT(MONTH FROM loan_date)::INT,
                        TO_CHAR(loan_date, 'FMMONTH'),
                        EXTRACT(YEAR FROM loan_date)::INT,
                        EXTRACT(DOW FROM loan_date) IN (0, 6)
                    FROM loans
                    ON CONFLICT (full_date) DO NOTHING
                """;

        return entityManager.createNativeQuery(query).executeUpdate();
    }

    private int loadDimCourse() {
        String query = """
                INSERT INTO warehouse.dim_course (course_id, course_name, course_code)
                SELECT course_id, course_name, course_code
                FROM courses
                WHERE course_id NOT IN (
                    SELECT course_id
                    FROM warehouse.dim_course
                )
                """;
        return entityManager.createNativeQuery(query).executeUpdate();
    }

    private int loadDimBook() {
        String query = """
                    INSERT INTO warehouse.dim_book (book_id, isbn, title, author, effective_from, is_current)
                SELECT id, isbn, title, author, CURRENT_DATE, true
                FROM books
                WHERE id NOT IN (
                    SELECT book_id FROM warehouse.dim_book WHERE is_current = true
                )
                """;
        return entityManager.createNativeQuery(query).executeUpdate();
    }

    private int loadDimStudent() {
        String query = """
                INSERT INTO warehouse.dim_student (student_id, student_number, course_key, study_year, effective_from, is_current)
                SELECT student.student_id, student.student_number, dimc.course_key, student.study_year, CURRENT_DATE, true
                FROM students student
                JOIN warehouse.dim_course dimc ON dimc.course_id = student.course_id
                WHERE student.student_id NOT IN (
                    SELECT student_id FROM warehouse.dim_student WHERE is_current = true
                )
                """;
        return entityManager.createNativeQuery(query).executeUpdate();
    }

    private int loadFactLoans() {
        String query = """
                INSERT INTO warehouse.fact_loans (loan_id, date_key, book_key, student_key, course_key, loan_duration, overdue, fine_amount, status, loan_year)
                SELECT
                    loan.id, dimd.date_key, dimb.book_key, dims.student_key, dimc.course_key,

                    CASE WHEN loan.return_date IS NOT NULL
                        THEN (loan.return_date - loan.loan_date)
                        ELSE NULL END,
                    CASE WHEN loan.status = 'OVERDUE' OR (loan.status = 'BORROWED' AND loan.due_date < CURRENT_DATE)
                        THEN true ELSE false END,
                loan.fine_amount,
                loan.status,
                EXTRACT(YEAR FROM loan.loan_date)::INT

                FROM loans loan
                JOIN warehouse.dim_date dimd
                    ON dimd.full_date = loan.loan_date
                JOIN warehouse.dim_book dimb
                    ON dimb.book_id = loan.book_id AND dimb.is_current = true
                JOIN students student
                    ON student.user_id = loan.user_id
                JOIN warehouse.dim_student dims
                    ON dims.student_id = student.student_id AND dims.is_current = true
                JOIN warehouse.dim_course dimc
                    ON dimc.course_id = student.course_id
                WHERE loan.id NOT IN (
                    SELECT loan_id FROM warehouse.fact_loans
                    )
                    """;

        return entityManager.createNativeQuery(query).executeUpdate();
    }

}
