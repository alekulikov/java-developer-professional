package ru.otus.atm.services;

import java.math.BigInteger;
import java.util.*;
import ru.otus.atm.enums.Banknote;

public class MoneyBoxServiceImpl implements MoneyBoxService {
    private final Map<Banknote, Integer> banknoteCells = new EnumMap<>(Banknote.class);

    public MoneyBoxServiceImpl(Banknote... banknotes) {
        Arrays.stream(banknotes).forEach(banknote -> banknoteCells.put(banknote, 0));
    }

    @Override
    public void putMoney(Map<Banknote, Integer> notes) {
        banknoteCells.forEach((key, value) -> banknoteCells.merge(key, notes.get(key), Integer::sum));
    }

    @Override
    public Map<Banknote, Integer> getMoney(BigInteger amount) {
        if (amount.compareTo(getSum()) > 0) {
            throw new IllegalStateException("Not enough money");
        }
        List<Banknote> banknotes = banknoteCells.keySet().stream()
                .sorted(Comparator.comparing(banknote -> banknote.denomination, Comparator.reverseOrder()))
                .toList();
        if (!amount.mod(banknotes.getLast().denomination).equals(BigInteger.ZERO)) {
            throw new IllegalStateException("Can't charge the required sum");
        }
        Map<Banknote, Integer> result = new EnumMap<>(Banknote.class);
        int chargedNotes;
        int requiredNotes;
        for (Banknote banknote : banknotes) {
            if (amount.compareTo(banknote.denomination) >= 0) {
                requiredNotes = amount.divide(banknote.denomination).intValue();
                chargedNotes = Math.min(requiredNotes, banknoteCells.get(banknote));
                amount = amount.subtract(banknote.denomination.multiply(BigInteger.valueOf(chargedNotes)));
                result.put(banknote, chargedNotes);
            } else {
                result.put(banknote, 0);
            }
        }
        for (Banknote banknote : banknotes) {
            banknoteCells.put(banknote, banknoteCells.get(banknote) - result.get(banknote));
        }
        return result;
    }

    @Override
    public BigInteger getSum() {
        return banknoteCells.entrySet().stream()
                .map(entry -> entry.getKey().denomination.multiply(BigInteger.valueOf(entry.getValue())))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
