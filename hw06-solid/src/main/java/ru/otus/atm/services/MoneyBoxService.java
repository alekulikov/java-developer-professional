package ru.otus.atm.services;

import ru.otus.atm.data.MoneyBox;

import java.math.BigInteger;
import java.util.List;

public interface MoneyBoxService {
    void putMoney(MoneyBox moneyBox, List<Integer> notes);

    List<Integer> getMoney(MoneyBox moneyBox, BigInteger amount);

    BigInteger getSum(MoneyBox moneyBox);
}
