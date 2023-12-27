package ru.otus.atm;

import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ru.otus.atm.enums.Banknote;
import ru.otus.atm.services.AtmService;
import ru.otus.atm.services.AtmServiceImpl;

@Slf4j
public class Main {
    public static void main(String[] args) {
        AtmService atmService =
                new AtmServiceImpl(Banknote.RUB100, Banknote.RUB500, Banknote.RUB1000, Banknote.RUB5000);
        log.info("At first ATM balance: {}", atmService.getBalance());
        atmService.putMoney(Map.of(
                Banknote.RUB100, 10,
                Banknote.RUB500, 10,
                Banknote.RUB1000, 10,
                Banknote.RUB5000, 10));
        log.info("Then ATM balance: {}", atmService.getBalance());
        log.info("Withdraw 59900 from an ATM: {}", atmService.getMoney(BigDecimal.valueOf(59900)));
        log.info("Then ATM balance: {}", atmService.getBalance());
    }
}
