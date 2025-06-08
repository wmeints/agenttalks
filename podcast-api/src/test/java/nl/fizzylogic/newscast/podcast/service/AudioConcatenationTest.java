package nl.fizzylogic.newscast.podcast.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class AudioConcatenationTest {
    @Inject
    AudioConcatenation audioConcatenation;

    @Test
    public void canConcatenateAudioFiles() {
        var audioFiles = List.of(new File("src/test/resources/audio/joop-fragment-01.mp3").getAbsolutePath(),
                new File("src/test/resources/audio/willem-fragment-01.mp3").getAbsolutePath());

        var outputFile = new File("data/concatenated-samples.mp3");

        audioConcatenation.concatenateAudioFiles(audioFiles, outputFile);

        assertTrue(outputFile.exists(), "Output file should be created by ffmpeg");
    }
}
