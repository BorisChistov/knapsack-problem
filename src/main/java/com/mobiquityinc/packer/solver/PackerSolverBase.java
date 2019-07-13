package com.mobiquityinc.packer.solver;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Package;
import com.mobiquityinc.packer.util.AggregatedItems;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This abstract class solves to implement common logic before solving the problem
 * It checks if item list is empty, has 1 item, or all items fits to package.
 * else it tries to solve the problem with implemented algorithm
 */
public abstract class PackerSolverBase implements PackerSolver {

    @Override
    public Set<Item> solve(
            Package input, SolverConfiguration config
    ) {
        AggregatedItems aggregatedItems = input.getItems()
                .stream()
                .filter(i -> i.getWeight().compareTo(input.getWeight()) <= 0)
                .collect(() -> new AggregatedItems(config), AggregatedItems::accept, AggregatedItems::combine);

        if (aggregatedItems.getItems().size() <= 1 || aggregatedItems.getTotalWeight().compareTo(input.getWeight()) <= 0) {
            return new HashSet<>(aggregatedItems.getItems());
        }
        return solve0(input.getWeight(), aggregatedItems.getItems(), config);
    }

    public abstract Set<Item> solve0(BigDecimal maxWeight, List<Item>items, SolverConfiguration config);

}
