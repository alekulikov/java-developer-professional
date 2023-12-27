package ru.otus.atm.services;

import java.math.BigDecimal;
import java.util.Map;
import ru.otus.atm.enums.Banknote;

public interface AtmService {
    BigDecimal putMoney(Map<Banknote, Integer> notes);

    Map<Banknote, Integer> getMoney(BigDecimal amount);

    BigDecimal getBalance();
}
