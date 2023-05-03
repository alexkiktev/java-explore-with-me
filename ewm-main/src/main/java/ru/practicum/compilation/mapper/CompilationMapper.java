package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(source = "events", target = "events")
    Compilation toCompilation(CompilationNewDto compilationNewDto);

    CompilationDto toCompilationDto(Compilation compilation);

    default Event fromLong(Long id) {
        Event event = new Event();
        event.setId(id);
        return event;
    }

}
