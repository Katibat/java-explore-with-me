package ru.practicum.explorewithme;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper extends ModelMapper {

    public Mapper() {
        setUp();
    }

    public <S, T> List<T> mapList(List<S> list, Class<T> targetClass) {
        return list.stream()
                .map(object -> super.map(object, targetClass))
                .collect(Collectors.toList());
    }

    private void setUp() {
        this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
}