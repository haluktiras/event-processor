package tech.pim.eventProcessor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import tech.pim.eventProcessor.api.StreamProcessor;
import tech.pim.eventProcessor.model.Article;
import tech.pim.eventProcessor.model.PickEvent;
import tech.pim.eventProcessor.model.Picker;
import tech.pim.eventProcessor.model.mapper.PickGroupMapper;
import tech.pim.eventProcessor.model.output.PickGroup;

import java.io.*;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class PickingStreamProcessor implements StreamProcessor {

    private final int maxEvents;
    private final Clock maxTime;

    public PickingStreamProcessor(int maxEvents, Duration maxTime) {
        this.maxEvents = maxEvents;
        this.maxTime = fixTheTime(maxTime);
    }

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    private Clock fixTheTime(Duration maxTime) {
        Clock fixedTime = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        return Clock.offset(fixedTime, maxTime);
    }

    @Override
    public void process(InputStream source, OutputStream sink) throws IOException {

        final Map<Picker, List<PickEvent>> events = processEvents(source);

        final List<PickGroup> groups = events.entrySet().stream()
                .map(this::mapToPickGroup)
                .collect(Collectors.toList());

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sink))) {
            writer.write(mapper.writeValueAsString(groups));
        }
    }

    private PickGroup mapToPickGroup(Map.Entry<Picker, List<PickEvent>> entry) {

        Picker picker = entry.getKey();

        List<PickEvent> events = entry.getValue().stream()
                .sorted(Comparator.comparing(PickEvent::getTimestamp))
                .collect(Collectors.toList());

        return PickGroupMapper.MAPPER.toPickGroup(picker, events);
    }

    private Map<Picker, List<PickEvent>> processEvents(final InputStream source) throws IOException {

        final SortedMap<Picker, List<PickEvent>> events = new TreeMap<>(
                Comparator.comparing(Picker::getActiveSince)
                        .thenComparing(Picker::getId)
        );
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(source))) {

            String line; int eventCount = 0;

            while (Objects.nonNull((line = reader.readLine())) && eventCount < maxEvents) {

                if (Instant.now().isAfter(maxTime.instant())) break;

                eventCount++;
                Optional<PickEvent> pickEvents = parseEvent(line);

                pickEvents.ifPresent(pickEvent -> {
                    if (shouldStorePickEvent(pickEvent)) {
                        List<PickEvent> articles = events.computeIfAbsent(pickEvent.getPicker(), k -> new ArrayList<>());
                        articles.add(pickEvent);
                    }
                });
            }
        }
        return events;
    }

    private boolean shouldStorePickEvent(PickEvent pickEvent) {
        return pickEvent.getArticle().getTemperatureZone() == Article.TemperatureZone.AMBIENT;
    }

    private Optional<PickEvent> parseEvent(String line) throws JsonProcessingException {

        if (StringUtils.isBlank(line)) return Optional.empty();

        final PickEvent event = mapper.readValue(line, PickEvent.class);
        return Optional.of(event);
    }
}
