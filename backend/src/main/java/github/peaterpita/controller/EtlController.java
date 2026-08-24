package github.peaterpita.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.peaterpita.service.EtlService;

@RestController
@RequestMapping("/api/etl")
public class EtlController {

    private final EtlService etlService;

    public EtlController(EtlService etlService) {
        this.etlService = etlService;
    }

    // ###########################################################
    // # /api/etl
    // # Endpoint to run the entire ETL chain of
    // # Loading data into warehouse dimensional tables
    // ###########################################################
    @PostMapping
    public ResponseEntity<?> run() {
        try {
            Map<String, Integer> result = etlService.run();
            return ResponseEntity.ok(result);
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(Map.of("error", err.getMessage()));
        }
    }

}
