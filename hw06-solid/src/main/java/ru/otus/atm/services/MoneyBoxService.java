package ru.otus.atm.services;

import java.math.BigInteger;
import java.util.Map;
import ru.otus.atm.enums.Banknote;

public interface MoneyBoxService {
    void putMoney(Map<Banknote, Integer> notes);

    Map<Banknote, Integer> getMoney(BigInteger amount);

    BigInteger getSum();
}
