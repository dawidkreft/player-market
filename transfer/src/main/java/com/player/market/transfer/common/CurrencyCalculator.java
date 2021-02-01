package com.player.market.transfer.common;

import com.player.market.transfer.team.entity.Currency;

public class CurrencyCalculator {

    /**
     * Method to calculate value between currency
     * @param destination currency to target calculate
     * @param source currency from calculate
     * @param value the value of money
     * @return calculated value of money in destination currency
     */
    public static double calculate(Currency destination, Currency source, double value) {
        double valueInPln = source.valueInRelationToPLN() * value;
        return valueInPln / destination.valueInRelationToPLN();
    }
}
