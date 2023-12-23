package ru.otus.atm.data;

import lombok.Getter;
import ru.otus.atm.enums.Banknote;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@Getter
public class MoneyBox {
    private final Map<Banknote, Integer> banknoteCells = new EnumMap<>(Banknote.class);

    public MoneyBox() {
        Arrays.stream(Banknote.values()).forEach(banknote -> banknoteCells.put(banknote, 0));
    }
}
