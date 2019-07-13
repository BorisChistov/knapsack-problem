package com.mobiquityinc.packer.model;

import java.math.BigDecimal;
import java.util.List;

public class Package {
    private final List<Item> items;
    private final BigDecimal weight;

    public Package(List<Item> items, BigDecimal weight) {
        this.items = items;
        this.weight = weight;
    }

    public List<Item> getItems() {
        return items;
    }

    public BigDecimal getWeight() {
        return weight;
    }
}
