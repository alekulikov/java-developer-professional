package ru.otus.atm.services;

import java.math.BigInteger;
import java.util.*;
import ru.otus.atm.data.MoneyBox;
import ru.otus.atm.enums.Banknote;

public class MoneyBoxServiceImpl implements MoneyBoxService {

    @Override
    public void putMoney(MoneyBox moneyBox, List<Integer> notes) {
        Map<Banknote, Integer> cells = moneyBox.getBanknoteCells();
        Arrays.stream(Banknote.values())
                .forEach(banknote -> cells.merge(banknote, notes.get(banknote.ordinal()), Integer::sum));
    }

    @Override
    public List<Integer> getMoney(MoneyBox moneyBox, BigInteger amount) {
        if (amount.compareTo(getSum(moneyBox)) > 0) {
            throw new IllegalStateException("Not enough money");
        }
        if (!amount.mod(BigInteger.valueOf(100L)).equals(BigInteger.ZERO)) {
            throw new IllegalStateException("Can't charge the required sum");
        }
        List<Integer> result = new ArrayList<>();
        List<Banknote> banknotes = Arrays.stream(Banknote.values())
                .sorted(Comparator.comparing(banknote -> banknote.denomination, Comparator.reverseOrder()))
                .toList();
        Map<Banknote, Integer> cells = moneyBox.getBanknoteCells();
        int chargedNotes;
        int requiredNotes;
        for (Banknote banknote : banknotes) {
            if (amount.compareTo(banknote.denomination) >= 0) {
                requiredNotes = amount.divide(banknote.denomination).intValue();
                chargedNotes = Math.min(requiredNotes, cells.get(banknote));
                amount = amount.subtract(banknote.denomination.multiply(BigInteger.valueOf(chargedNotes)));
                result.add(chargedNotes);
            } else {
                result.add(0);
            }
        }
        for (int i = 0; i < banknotes.size(); i++) {
            cells.put(banknotes.get(i), cells.get(banknotes.get(i)) - result.get(i));
        }
        return result;
    }

    @Override
    public BigInteger getSum(MoneyBox moneyBox) {
        return Arrays.stream(Banknote.values())
                .map(banknote -> BigInteger.valueOf(moneyBox.getBanknoteCells().get(banknote))
                        .multiply(banknote.denomination))
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
