package com.mobiquityinc.packer.solver;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.model.Item;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Brute force packer.
 * Complicity O(2^n) where n is number of items
 * Complicity with given constraints
 * 2 ^ 15 = 32 768
 */
public class BFPackerSolver extends PackerSolverBase {

    @Override
    public Set<Item> solve0(
            BigDecimal maxWeight, List<Item> items, SolverConfiguration config
    ) {
        SolutionContext result = solve0(
                new SolutionContext(maxWeight, new LinkedList<>(items)),
                config
        );
        return new HashSet<>(result.items);
    }


    private SolutionContext solve0(SolutionContext ctx, SolverConfiguration config) {
        if (ctx.totalWeight.doubleValue() == 0 || ctx.items.isEmpty()) {
            return new SolutionContext(config);
        }
        LinkedList<Item> clonedItems = new LinkedList<>(ctx.items);
        Item item = clonedItems.pop();

        if (item.getWeight().compareTo(ctx.totalWeight) > 0) {
            return solve0(new SolutionContext(ctx.totalWeight, clonedItems), config);
        } else {
            SolutionContext withItem = solve0(
                    new SolutionContext(
                            ctx.totalWeight.subtract(item.getWeight()),
                            clonedItems
                    ),
                    config
            );
            BigDecimal costWithItem = item.getCost().add(withItem.totalCost);
            SolutionContext woItem = solve0(new SolutionContext(ctx.totalWeight, clonedItems), config);
            BigDecimal costWoItem = woItem.totalCost;

            if (costWithItem.compareTo(costWoItem) > 0) {
                withItem.totalCost = costWithItem;
                withItem.items.add(item);
                return withItem;
            } else {
                woItem.totalCost = costWoItem;
                return woItem;
            }
        }
    }

    private class SolutionContext {
        private BigDecimal totalWeight;
        private BigDecimal totalCost;
        private Queue<Item> items;

        SolutionContext(SolverConfiguration config) {
            totalWeight = new BigDecimal(0d).setScale(config.getScale(), config.getRoundingMode());
            totalCost = new BigDecimal(0d).setScale(config.getScale(), config.getRoundingMode());
            this.items = new LinkedList<>();
        }

        SolutionContext(BigDecimal totalWeight, Queue<Item> items) {
            this.totalWeight = totalWeight;
            this.items = items;
        }
    }
}
