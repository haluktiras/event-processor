package tech.pim.eventProcessor.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import tech.pim.eventProcessor.model.PickEvent;
import tech.pim.eventProcessor.model.Picker;
import tech.pim.eventProcessor.model.output.PickGroup;

import java.util.List;

@Mapper(uses = PickMapper.class)
public interface PickGroupMapper {

    PickGroupMapper MAPPER = Mappers.getMapper(PickGroupMapper.class);

    @Mappings({
            @Mapping(target = "pickerName", source = "picker.name"),
            @Mapping(target = "activeSince", source = "picker.activeSince"),
            @Mapping(target = "picks", source = "pickEvent")
    })
    PickGroup toPickGroup(Picker picker, List<PickEvent> pickEvent);
}
