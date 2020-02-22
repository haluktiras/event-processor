package tech.pim.eventProcessor.impl;

import com.google.auto.service.AutoService;
import tech.pim.eventProcessor.api.EventProcessorFactory;
import tech.pim.eventProcessor.api.StreamProcessor;

import java.time.Duration;

@AutoService(EventProcessorFactory.class)
public final class PickingEventProcessorFactory implements EventProcessorFactory {
    @Override
    public StreamProcessor createProcessor(int maxEvents, Duration maxTime) {
        return new PickingStreamProcessor(maxEvents, maxTime);
    }
}
