package ru.otus.dataprocessor;

import java.util.*;
import java.util.stream.Collectors;
import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        return data.stream()
                .sorted(Comparator.comparing(Measurement::name))
                .collect(Collectors.groupingBy(
                        Measurement::name, LinkedHashMap::new, Collectors.summingDouble(Measurement::value)));
    }
}
