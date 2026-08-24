
package github.peaterpita.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.peaterpita.service.BackupService;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    // ###########################################################
    // # /api/backup GET
    // # Return a list of all the backups available
    // ###########################################################
    @GetMapping()
    public ResponseEntity<?> listBackups() {
        return ResponseEntity.ok(backupService.listBackups());

    }

    // ###########################################################
    // # /api/backup POST
    // # Create a new backup file using the service and return filename
    // ###########################################################
    @PostMapping()
    public ResponseEntity<?> createBackup() {
        try {
            String filename = backupService.createBackup();
            return ResponseEntity.ok(Map.of("success", true, "filename", filename));

        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(Map.of("error", err.getMessage()));
        }

    }

    // ###########################################################
    // # /api/backup/activate/{id}
    // # Restore database to a previous backup
    // ###########################################################
    @PostMapping("/activate/{filename}")
    public ResponseEntity<?> activateBackup(@PathVariable String filename) {
        try {
            backupService.activateBackup(filename);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(Map.of("error", err.getMessage()));
        }

    }
}
