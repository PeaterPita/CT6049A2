package github.peaterpita.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.peaterpita.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/loans/trend")
    public ResponseEntity<List<Map<String, Object>>> loanTrend(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(warehouseService.getLoanTrend(start, end));
    }

    @GetMapping("/fines/trend")
    public ResponseEntity<List<Map<String, Object>>> fineRevenue(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(warehouseService.getFineRevenue(start, end));
    }

    @GetMapping("/loans/overdue")
    public ResponseEntity<List<Map<String, Object>>> overdue() {
        return ResponseEntity.ok(warehouseService.getOverdue());
    }

    @GetMapping("/books/popular")
    public ResponseEntity<List<Map<String, Object>>> popular(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(warehouseService.getPopular(start, end));
    }

    @GetMapping("/loans/duration")
    public ResponseEntity<List<Map<String, Object>>> duration(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(warehouseService.getLoanDuration(start, end));
    }

    @GetMapping("/loans/course")
    public ResponseEntity<List<Map<String, Object>>> courseEngagment(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(warehouseService.getCourseEngagment(start, end));
    }

    @GetMapping("/loans/year")
    public ResponseEntity<List<Map<String, Object>>> yearEngagment(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(warehouseService.getYearEngagement(start, end));
    }

    @GetMapping("/loans/peak")
    public ResponseEntity<List<Map<String, Object>>> peakLoans(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(warehouseService.getPeakDays(start, end));
    }

}
