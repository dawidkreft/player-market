package com.player.market.transfer.team.entity;

public enum Currency {
    EUR(4.40),
    USD(3.80),
    GBP(5.00);

    private final Double valueInRelationToPLN;

    private Currency(Double valueInRelationToPLN) {
        this.valueInRelationToPLN = valueInRelationToPLN;
    }

    public Double valueInRelationToPLN() {
        return valueInRelationToPLN;
    }

}
