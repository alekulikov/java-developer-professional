package ru.otus.atm.enums;

import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
public enum Banknote {
    RUB100(BigInteger.valueOf(100), Currency.RUB),
    RUB500(BigInteger.valueOf(500), Currency.RUB),
    RUB1000(BigInteger.valueOf(1000), Currency.RUB),
    RUB5000(BigInteger.valueOf(5000), Currency.RUB);

    public final BigInteger denomination;
    public final Currency currency;
}
