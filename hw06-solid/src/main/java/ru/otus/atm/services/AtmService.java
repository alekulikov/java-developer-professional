package ru.otus.atm.services;

import ru.otus.atm.data.Atm;

import java.math.BigDecimal;
import java.util.List;

public interface AtmService {
    BigDecimal putMoney(Atm atm, List<Integer> notes);

    List<Integer> getMoney(Atm atm, BigDecimal amount);

    BigDecimal getBalance(Atm atm);
}
