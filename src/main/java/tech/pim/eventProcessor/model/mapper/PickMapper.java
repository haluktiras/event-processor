package tech.pim.eventProcessor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import tech.pim.eventProcessor.model.PickEvent;
import tech.pim.eventProcessor.model.output.Pick;

@Mapper
public interface PickMapper {
    @Mappings({
            @Mapping(target = "articleName", expression = "java(pickEvent.getArticle().getName().toUpperCase())"),
            @Mapping(target = "timestamp", source = "pickEvent.timestamp")
    })
    Pick toPick(PickEvent pickEvent);
}
