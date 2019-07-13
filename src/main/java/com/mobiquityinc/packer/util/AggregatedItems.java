package com.mobiquityinc.packer.util;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.model.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AggregatedItems implements Consumer<Item> {

    private List<Item> items = new ArrayList<>();
    private BigDecimal totalWeight;

    public AggregatedItems(SolverConfiguration config) {
        this.totalWeight = new BigDecimal(0).setScale(config.getScale(), config.getRoundingMode());
    }

    @Override
    public void accept(Item item) {
        items.add(item);
        totalWeight = totalWeight.add(item.getWeight());
    }

    public void combine(AggregatedItems aggregatedItems) {
        this.items.addAll(aggregatedItems.items);
        this.totalWeight = this.totalWeight.add(aggregatedItems.totalWeight);
    }

    public List<Item> getItems() {
        return items;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }
}
