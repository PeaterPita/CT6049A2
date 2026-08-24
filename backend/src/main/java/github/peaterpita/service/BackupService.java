package github.peaterpita.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class BackupService {

    private String backupPath = "./Backups/";

    private void ensureBackupDir() {
        File dir = new File(backupPath);
        if (!dir.exists())
            dir.mkdirs();
    }

    public List<Map<String, Object>> listBackups() {
        ensureBackupDir();

        File dir = new File(backupPath);
        File[] files = dir.listFiles((unused, name) -> name.endsWith(".sql"));

        if (files == null)
            return List.of();

        return Arrays.stream(files)
                .sorted((a, b) -> Long.compare(b.lastModified(), a.lastModified()))
                .map(file -> Map.<String, Object>of(
                        "filename", file.getName(),
                        "size", file.length() / 1204,
                        "modified", file.lastModified()))
                .toList();

    }

    public String createBackup() {
        ensureBackupDir();
        String filename = "db" + LocalDateTime.now().toString() + ".sql";
        Path target = Paths.get(backupPath, filename);

        ProcessBuilder process = new ProcessBuilder(
                "pg_dump",
                "-h", "localhost",
                "-p", "5432",
                "-U", "libuser",
                "-d", "libdb",
                "-f", target.toString());

        process.environment().put("PGPASSWORD", "libpass");
        process.redirectErrorStream(true);

        try {
            Process proc = process.start();
            int exit = proc.waitFor();

            if (exit != 0) {
                String err = new String(proc.getInputStream().readAllBytes());
                throw new RuntimeException("pg_dump failed" + err);
            }

            return filename;
        } catch (IOException err) {
            throw new RuntimeException("Failure with IO maybe pg_dump isnt in path", err);
        } catch (InterruptedException err) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted Unexpectealy: ", err);
        }

    }

    public void activateBackup(String filename) {
        ensureBackupDir();
        if (filename.contains("/")) {
            System.out.println("bad path");
            throw new IllegalArgumentException("Invalid name");
        }

        Path target = Paths.get(backupPath, filename);
        if (!Files.exists(target)) {
            System.out.println("file not found");
            throw new RuntimeException("File no found");
        }

        ProcessBuilder process = new ProcessBuilder(
                "psql",
                "-h", "localhost",
                "-p", "5432",
                "-U", "libuser",
                "-d", "libdb",
                "-f", target.toString());

        process.environment().put("PGPASSWORD", "libpass");
        process.redirectErrorStream(true);

        try {
            Process proc = process.start();
            int exit = proc.waitFor();

            if (exit != 0) {
                String err = new String(proc.getInputStream().readAllBytes());
                throw new RuntimeException("psql failed" + err);
            }
        } catch (IOException err) {
            throw new RuntimeException("IO failure", err);
        } catch (InterruptedException err) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted Unexpectealy: ", err);
        }

    }

}
