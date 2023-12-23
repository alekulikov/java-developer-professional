package ru.otus.atm.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.otus.atm.data.Atm;

public class AtmServiceImpl implements AtmService {
    private final MoneyBoxService moneyBoxService;

    public AtmServiceImpl(MoneyBoxService moneyBoxService) {
        this.moneyBoxService = moneyBoxService;
    }

    @Override
    public BigDecimal putMoney(Atm atm, List<Integer> notes) {
        List<Integer> arrangedNotes = new ArrayList<>(notes);
        for (int i = 0; i < 4 - arrangedNotes.size(); i++) {
            arrangedNotes.add(0);
        }
        moneyBoxService.putMoney(atm.getMoneyBox(), arrangedNotes);
        return getBalance(atm);
    }

    @Override
    public List<Integer> getMoney(Atm atm, BigDecimal amount) {
        return moneyBoxService.getMoney(atm.getMoneyBox(), amount.toBigIntegerExact());
    }

    @Override
    public BigDecimal getBalance(Atm atm) {
        return new BigDecimal(moneyBoxService.getSum(atm.getMoneyBox()));
    }
}
