package tech.pim.eventProcessor.impl;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import tech.pim.eventProcessor.api.EventProcessorFactory;
import tech.pim.eventProcessor.api.StreamProcessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class PickingEventProcessorFactoryTest {
    //@Disabled
    @ParameterizedTest
    @MethodSource("happyPathTestCaseInputProvider")
    void testHappyPath(
            int maxEvents,
            Duration maxTime,
            String inputResource,
            String expectedOutputResource)
            throws IOException, JSONException {
        try (EventProcessorFactory factory = new PickingEventProcessorFactory();
                StreamProcessor processor = factory.createProcessor(maxEvents, maxTime);
                InputStream source = getClass().getResourceAsStream(inputResource);
                ByteArrayOutputStream sink = new ByteArrayOutputStream()) {
            processor.process(source, sink);
            String expectedOutput = loadResource(expectedOutputResource);
            String actualOutput = new String(sink.toByteArray(), StandardCharsets.UTF_8);
            JSONAssert.assertEquals(expectedOutput, actualOutput, JSONCompareMode.STRICT);
        }
    }

    static Stream<Arguments> happyPathTestCaseInputProvider() {
        return Stream.of(
                Arguments.of(
                        100,
                        Duration.ofSeconds(30),
                        "happy-path-input.json-stream",
                        "happy-path-output.json"));
    }

    @ParameterizedTest
    @MethodSource("alternatePathTestCaseInputProvider")
    void testAlternatePath(
            int maxEvents,
            Duration maxReadTime,
            String inputResource,
            String expectedOutputResource)
            throws IOException, JSONException {
        long startTime = System.currentTimeMillis();
        try (EventProcessorFactory factory = new PickingEventProcessorFactory();
             StreamProcessor processor = factory.createProcessor(maxEvents, maxReadTime);
             InputStream source = getClass().getResourceAsStream(inputResource);
             ByteArrayOutputStream sink = new ByteArrayOutputStream()) {
            processor.process(source, sink);
            String expectedOutput = loadResource(expectedOutputResource);
            String actualOutput = new String(sink.toByteArray(), StandardCharsets.UTF_8);
            JSONAssert.assertEquals(expectedOutput, actualOutput, JSONCompareMode.STRICT);
        }
    }

    static Stream<Arguments> alternatePathTestCaseInputProvider() {
        return Stream.of(
                Arguments.of(
                        100,
                        Duration.ofSeconds(5),
                        "alternate-path-input.json-stream",
                        "alternate-path-output.json"));
    }

    @ParameterizedTest
    @MethodSource("maxEventsTestCaseInputProvider")
    void testMaximumEvents(
            int maxEvents,
            Duration maxReadTime,
            String inputResource,
            String expectedOutputResource)
            throws IOException, JSONException {
        try (EventProcessorFactory factory = new PickingEventProcessorFactory();
             StreamProcessor processor = factory.createProcessor(maxEvents, maxReadTime);
             InputStream source = getClass().getResourceAsStream(inputResource);
             ByteArrayOutputStream sink = new ByteArrayOutputStream()) {
            processor.process(source, sink);
            String expectedOutput = loadResource(expectedOutputResource);
            String actualOutput = new String(sink.toByteArray(), StandardCharsets.UTF_8);
            JSONAssert.assertEquals(expectedOutput, actualOutput, JSONCompareMode.STRICT);
        }
    }

    static Stream<Arguments> maxEventsTestCaseInputProvider() {
        return Stream.of(
                Arguments.of(
                        2,
                        Duration.ofSeconds(1),
                        "alternate-path-input.json-stream",
                        "limited-path-output.json"));
    }

    @ParameterizedTest
    @MethodSource("emptyTestCaseInputProvider")
    void testEmpty(
            int maxEvents,
            Duration maxReadTime,
            String inputResource,
            String expectedOutputResource)
            throws IOException, JSONException {
        try (EventProcessorFactory factory = new PickingEventProcessorFactory();
             StreamProcessor processor = factory.createProcessor(maxEvents, maxReadTime);
             InputStream source = getClass().getResourceAsStream(inputResource);
             ByteArrayOutputStream sink = new ByteArrayOutputStream()) {
            processor.process(source, sink);
            String expectedOutput = loadResource(expectedOutputResource);
            String actualOutput = new String(sink.toByteArray(), StandardCharsets.UTF_8);
            JSONAssert.assertEquals(expectedOutput, actualOutput, JSONCompareMode.STRICT);
        }
    }

    static Stream<Arguments> emptyTestCaseInputProvider() {
        return Stream.of(
                Arguments.of(
                        100,
                        Duration.ofSeconds(2),
                        "empty-input.json-stream",
                        "empty-output.json"));
    }

    private String loadResource(String resource) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resource);
                Scanner scanner = new Scanner(is)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    /** Verifies that precisely one {@link EventProcessorFactory} can be service-loaded. */
    @Test
    void testServiceLoading() {
        Iterator<EventProcessorFactory> factories =
                ServiceLoader.load(EventProcessorFactory.class).iterator();
        assertTrue(factories.hasNext(), "No EventProcessorFactory is service-loaded");
        factories.next();
        assertFalse(factories.hasNext(), "More than one EventProcessorFactory is service-loaded");
    }
}
