package com.player.market.transfer.common;

import com.player.market.transfer.team.entity.Currency;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyCalculatorTest {

    @Test
    public void shouldCorrectCalculateBetweenEuroToUsd() {
        double calculate = CurrencyCalculator.calculate(Currency.EUR, Currency.USD, 500);

        Assertions.assertThat(calculate).isEqualTo(431.818, Offset.offset(0.01));
    }
}
