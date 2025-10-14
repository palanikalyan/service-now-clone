package com.requestmanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ShellExecutorService {

    private static final Logger logger = LoggerFactory.getLogger(ShellExecutorService.class);

    public void installSoftware(String softwareName, String hostname) throws IOException, InterruptedException {
        logger.info("Starting installation of {} on {}", softwareName, hostname);

        // Get the script path from resources
        ClassPathResource resource = new ClassPathResource("scripts/install.sh");
        String scriptPath = resource.getFile().getAbsolutePath();

        // Build the command
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", scriptPath, softwareName, hostname);

        // Execute the process
        Process process = processBuilder.start();

        // Read output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("Script output: {}", line);
            }
        }

        // Read error output
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                logger.error("Script error: {}", line);
            }
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            logger.info("Installation completed successfully for {} on {}", softwareName, hostname);
        } else {
            logger.error("Installation failed with exit code: {}", exitCode);
            throw new RuntimeException("Script execution failed with exit code: " + exitCode);
        }
    }
}