package ru.otus.atm;

import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.otus.atm.data.Atm;
import ru.otus.atm.data.MoneyBox;
import ru.otus.atm.services.AtmService;
import ru.otus.atm.services.AtmServiceImpl;
import ru.otus.atm.services.MoneyBoxService;
import ru.otus.atm.services.MoneyBoxServiceImpl;

@Slf4j
public class Main {
    public static void main(String[] args) {
        MoneyBoxService moneyBoxService = new MoneyBoxServiceImpl();
        AtmService atmService = new AtmServiceImpl(moneyBoxService);
        Atm atm = new Atm(new MoneyBox());

        log.info("At first ATM balance: {}", atmService.getBalance(atm));

        atmService.putMoney(atm, List.of(10, 10, 10, 10));
        log.info("Then ATM balance: {}", atmService.getBalance(atm));

        log.info("Withdraw 59900 from an ATM: {}", atmService.getMoney(atm, BigDecimal.valueOf(59900)));
        log.info("Then ATM balance: {}", atmService.getBalance(atm));
    }
}
