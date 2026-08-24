package github.peaterpita.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class WarehouseService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> getLoanTrend(LocalDate start, LocalDate end) {
        String querry = """
                    SELECT dimd.year, dimd.month, dimd.month_name, COUNT(*) AS total_loans
                    FROM warehouse.fact_loans factl
                    JOIN warehouse.dim_date dimd ON factl.date_key = dimd.date_key
                    WHERE (CAST(:start AS DATE) IS NULL OR dimd.full_date >= :start)
                        AND (CAST(:end AS DATE) IS NULL OR dimd.full_date <= :end)
                    GROUP BY dimd.year, dimd.month, dimd.month_name
                    ORDER BY dimd.year, dimd.month
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(querry)
                .setParameter("start", start).setParameter("end", end)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "year", row[0],
                "month", row[1],
                "monthName", row[2],
                "totalLoans", row[3])).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getOverdue() {
        String query = """
                SELECT dimc.course_name, COUNT(*) AS overdue_count,
                       COALESCE(SUM(factl.fine_amount), 0) AS total_fines
                FROM warehouse.fact_loans factl
                JOIN warehouse.dim_course dimc ON factl.course_key = dimc.course_key
                JOIN warehouse.dim_date dimd ON factl.date_key = dimd.date_key
                WHERE factl.overdue = true
                GROUP BY dimc.course_name
                ORDER BY overdue_count DESC
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(query)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "courseName", row[0],
                "overdueCount", row[1],
                "totalFines", row[2])).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPopular(LocalDate start, LocalDate end) {
        String query = """
                SELECT dimb.title, dimb.author, COUNT(*) AS loan_count
                FROM warehouse.fact_loans factl
                JOIN warehouse.dim_book dimb ON factl.book_key = dimb.book_key
                JOIN warehouse.dim_date dimd ON factl.date_key = dimd.date_key
                WHERE (CAST(:start AS DATE) IS NULL OR dimd.full_date >= :start)
                  AND (CAST(:end AS DATE) IS NULL OR dimd.full_date <= :end)
                GROUP BY dimb.title, dimb.author
                ORDER BY loan_count DESC
                LIMIT 10
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(query)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "title", row[0],
                "author", row[1],
                "loanCount", row[2])).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getLoanDuration(LocalDate start, LocalDate end) {
        String query = """
                SELECT dimc.course_name, ROUND(AVG(factl.loan_duration), 1) AS avg
                FROM warehouse.fact_loans factl
                JOIN warehouse.dim_course dimc ON factl.course_key = dimc.course_key
                JOIN warehouse.dim_date dimd ON factl.date_key = dimd.date_key
                WHERE factl.loan_duration IS NOT NULL
                  AND (CAST(:start AS DATE) IS NULL OR dimd.full_date >= :start)
                  AND (CAST(:end AS DATE) IS NULL OR dimd.full_date <= :end)
                GROUP BY dimc.course_name
                ORDER BY avg DESC
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(query)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "courseName", row[0],
                "avg", row[1] != null ? row[1] : 0.0)).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getCourseEngagment(LocalDate start, LocalDate end) {
        String query = """
                SELECT dimc.course_name, dimc.course_code, COUNT(*) AS loan_count
                FROM warehouse.fact_loans factl
                JOIN warehouse.dim_course dimc ON factl.course_key = dimc.course_key
                JOIN warehouse.dim_date dimd ON factl.date_key = dimd.date_key
                WHERE (CAST(:start AS DATE) IS NULL OR dimd.full_date >= :start)
                  AND (CAST(:end AS DATE) IS NULL OR dimd.full_date <= :end)
                GROUP BY dimc.course_name, dimc.course_code
                ORDER BY loan_count DESC
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(query)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "courseName", row[0],
                "courseCode", row[1],
                "loanCount", row[2])).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getYearEngagement(LocalDate start, LocalDate end) {
        String query = """
                SELECT dims.study_year, COUNT(*) AS loan_count
                FROM warehouse.fact_loans factl
                JOIN warehouse.dim_student dims ON factl.student_key = dims.student_key
                JOIN warehouse.dim_date dimd ON factl.date_key = dimd.date_key
                WHERE (CAST(:start AS DATE) IS NULL OR dimd.full_date >= :start)
                  AND (CAST(:end AS DATE) IS NULL OR dimd.full_date <= :end)
                GROUP BY dims.study_year
                ORDER BY dims.study_year
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(query)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "studyYear", row[0],
                "loanCount", row[1])).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getFineRevenue(LocalDate start, LocalDate end) {
        String querry = """
                    SELECT dimd.year, dimd.month, dimd.month_name, dimc.course_name, COALESCE(SUM(factl.fine_amount), 0) AS total_fines
                    FROM warehouse.fact_loans factl
                    JOIN warehouse.dim_date dimd on factl.date_key = dimd.date_key
                    JOIN warehouse.dim_course dimc ON factl.course_key = dimc.course_key
                    WHERE factl.fine_amount > 0 AND

                         (CAST(:start AS DATE) IS NULL OR dimd.full_date >= :start)
                          AND (CAST(:end AS DATE) IS NULL OR dimd.full_date <= :end)
                          GROUP BY dimd.year, dimd.month, dimd.month_name, dimc.course_name
                          ORDER BY dimd.year, dimd.month

                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(querry).setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "year", row[0],
                "month", row[1],
                "monthName", row[2],
                "courseName", row[3],
                "totalFines", row[4])).collect(Collectors.toList());

    }

    public List<Map<String, Object>> getPeakDays(LocalDate start, LocalDate end) {
        String querry = """
                    SELECT dimd.day_name, dimd.day_of_week, COUNT(*) AS loan_count
                    FROM warehouse.fact_loans factl
                    JOIN warehouse.dim_date dimd ON factl.date_key = dimd.date_key
                     WHERE (CAST(:start AS DATE) IS NULL OR dimd.full_date >= :start)
                      AND (CAST(:end AS DATE) IS NULL OR dimd.full_date <= :end)
                    GROUP BY dimd.day_name, dimd.day_of_week
                    ORDER BY dimd.day_of_week
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(querry)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return results.stream().map(row -> Map.of(
                "dayName", row[0],
                "dayOfWeek", row[1],
                "loanCount", row[2])).collect(Collectors.toList());

    }

}
