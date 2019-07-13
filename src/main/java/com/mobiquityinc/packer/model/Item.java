package com.mobiquityinc.packer.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Item {
    private final int id;
    private final BigDecimal weight;
    private final BigDecimal cost;

    public Item(int id, BigDecimal weight, BigDecimal cost) {
        this.id = id;
        this.weight = weight;
        this.cost = cost;
    }

    public Item(int id, double weight, double cost, int scale, RoundingMode roundingMode) {
        this.id = id;
        this.weight = new BigDecimal(weight).setScale(scale, roundingMode);
        this.cost = new BigDecimal(cost).setScale(scale, roundingMode);
    }

    public int getId() {
        return id;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public BigDecimal getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                Objects.equals(weight, item.weight) &&
                Objects.equals(cost, item.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, cost);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }
}
