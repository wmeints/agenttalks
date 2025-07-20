package nl.infosupport.agenttalks.podcast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AudioConcatenation {
    @ConfigProperty(name = "agenttalks.locations.ffmpeg")
    String ffmpegBinaryPath;

    @ConfigProperty(name = "agenttalks.locations.audio-files")
    String outputDirectoryPath;

    Logger logger = Logger.getLogger(AudioConcatenation.class);

    public String concatenateAudioFiles(List<String> inputFilePaths) {
        String outputFileName = String.format("%s.mp3", UUID.randomUUID().toString());
        File outputFile = new File(outputDirectoryPath, outputFileName);

        List<String> commandLine = List.of(
                ffmpegBinaryPath,
                "-f", "concat",
                "-safe", "0",
                "-i", createFileList(inputFilePaths).getAbsolutePath(),
                "-c", "copy",
                outputFile.getAbsolutePath());

        var ffmpegProcessBuilder = new ProcessBuilder(commandLine);
        ffmpegProcessBuilder.redirectErrorStream(true);

        try {
            var ffmpegProcess = ffmpegProcessBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ffmpegProcess.getInputStream()));
            StringBuilder logMessageBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                logMessageBuilder.append(line).append(System.lineSeparator());
            }

            int exitCode = ffmpegProcess.waitFor();

            if (exitCode != 0) {
                logger.errorf("FFmpeg process failed with exit code %d. Output: %s", exitCode,
                        logMessageBuilder.toString());
                throw new RuntimeException("FFmpeg process failed with exit code " + exitCode);
            } else {
                logger.infof("FFmpeg process completed successfully. Output: %s", logMessageBuilder.toString());
            }

            return outputFile.getAbsolutePath();
        } catch (Exception e) {
            logger.error("Error during audio concatenation", e);
            throw new RuntimeException("Error during audio concatenation", e);
        }
    }

    private File createFileList(List<String> inputFilePaths) {
        try {
            var tempFile = Files.createTempFile("ffmpeg_file_list_", ".txt");

            StringBuilder fileList = new StringBuilder();

            for (String filePath : inputFilePaths) {
                fileList.append("file '").append(new File(filePath).getAbsolutePath()).append("'\n");
            }

            Files.writeString(tempFile, fileList.toString());

            return tempFile.toFile();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to create temporary file for ffmpeg file list", e);
        }
    }
}
