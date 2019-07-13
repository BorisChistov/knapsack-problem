package com.mobiquityinc.packer.solver;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dynamic programing packer solver
 * ref: https://en.wikipedia.org/wiki/Knapsack_problem#Dynamic_programming_in-advance_algorithm
 * Because of decimal weights we have to scale numbers to integers
 * It increases the complicity of algorithm to O(n * W * 10^scale) where
 * n - number of elements
 * W - package weight
 * scale - scale of decimals
 * Complicity with given constraints
 * 15 * 100 * 100 = 150 000 iterations
 * With given constraints this algorithm is less effective
 * as brute force algorithm com.mobiquityinc.packer.solver.BFPackerSolver
 */
public class DPPackerSolver extends PackerSolverBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(DPPackerSolver.class);

    @Override
    public Set<Item> solve0(
            BigDecimal maxWeight, List<Item> items, SolverConfiguration config
    ) {
        items = items.stream().sorted(Comparator.comparing(Item::getWeight)).collect(Collectors.toList());
        // We scale maxWeight and items cost and weight to integers
        int scaleFactor = Math.toIntExact(Math.round(Math.pow(10, config.getScale())));
        int maxWeightScaled = scaledValue(maxWeight, scaleFactor);
        LOGGER.trace("Scale factor: {}, maxWeightScaled: {}", scaleFactor, maxWeightScaled);
        int[][] result = new int[items.size() + 1][maxWeightScaled + 1];

        for (int elIndex = 0; elIndex <= items.size(); elIndex++) {
            for (int weight = 0; weight <= maxWeightScaled; weight++) {
                if (elIndex == 0 || weight == 0) {
                    result[elIndex][weight] = 0;
                } else if (scaledValue(items.get(elIndex - 1).getWeight(), scaleFactor) <= weight) {
                    Item item = items.get(elIndex - 1);
                    int prevItemCost = result[elIndex - 1][weight - scaledValue(item.getWeight(), scaleFactor)];
                    result[elIndex][weight] = Math.max(
                            scaledValue(item.getCost(), scaleFactor) + prevItemCost,
                            result[elIndex - 1][weight]
                    );
                } else {
                    result[elIndex][weight] = result[elIndex - 1][weight];
                }

            }
        }

        prettyPrintArrays(result);

        return extractResult(result, items, scaleFactor);
    }

    private Set<Item> extractResult(int[][] result, List<Item> items, int scaleFactor) {
        Set<Item> resultItems = new HashSet<>();
        int row = result.length - 1;
        int col = result[row].length - 1;
        int lastValue = result[row][col];
        for (int index = items.size(); index > 0 && lastValue > 0; index--) {
            if (lastValue != result[index - 1][col]) {
                LOGGER.trace("row: {}, col: {}", index - 1, col);
                Item item = items.get(index - 1);
                lastValue = lastValue - scaledValue(item.getCost(), scaleFactor);
                col = col - scaledValue(item.getWeight(), scaleFactor);

                resultItems.add(item);
            }
        }
        return resultItems;
    }


    private int scaledValue(BigDecimal value, int scaleFactor) {
        return value.multiply(new BigDecimal(scaleFactor)).intValue();
    }

    private void prettyPrintArrays(int[][] array) {
        if (LOGGER.isTraceEnabled()) {
            for (int[] ints : array) {
                LOGGER.trace(Arrays.toString(ints));
            }
        }
    }
}
