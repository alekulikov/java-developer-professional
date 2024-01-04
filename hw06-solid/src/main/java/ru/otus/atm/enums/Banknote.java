package ru.otus.atm.enums;

import java.math.BigInteger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Banknote {
    RUB100(BigInteger.valueOf(100)),
    RUB500(BigInteger.valueOf(500)),
    RUB1000(BigInteger.valueOf(1000)),
    RUB5000(BigInteger.valueOf(5000));

    public final BigInteger denomination;
}
