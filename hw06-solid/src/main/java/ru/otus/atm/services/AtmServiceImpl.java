package ru.otus.atm.services;

import java.math.BigDecimal;
import java.util.Map;
import ru.otus.atm.enums.Banknote;

public class AtmServiceImpl implements AtmService {
    private final MoneyBoxService moneyBoxService;

    public AtmServiceImpl(Banknote... banknotes) {
        this.moneyBoxService = new MoneyBoxServiceImpl(banknotes);
    }

    @Override
    public BigDecimal putMoney(Map<Banknote, Integer> notes) {
        moneyBoxService.putMoney(notes);
        return getBalance();
    }

    @Override
    public Map<Banknote, Integer> getMoney(BigDecimal amount) {
        return moneyBoxService.getMoney(amount.toBigIntegerExact());
    }

    @Override
    public BigDecimal getBalance() {
        return new BigDecimal(moneyBoxService.getSum());
    }
}
