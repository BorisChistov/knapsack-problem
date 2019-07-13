package com.mobiquityinc.packer.solver;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mobiquityinc.packer.TestBase;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Package;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PackerSolverTestBase extends TestBase {

    @Test
    public void noItemsInput() {
        Package input = new Package(Collections.emptyList(), new BigDecimal(10));
        Set<Item> result = solver().solve(input, solverConfig());

        Assertions
                .assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void oneItemThatNotFitsPackageSize() {
        List<Item> items = Lists.newArrayList(createItem(1, 25d, 30d));

        Package input = new Package(items, new BigDecimal(20));

        Set<Item> result = solver().solve(input, solverConfig());

        Assertions
                .assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void oneItemThatFitsPackageSize() {
        List<Item> items = Lists.newArrayList(createItem(1, 15d, 30d));

        Package input = new Package(items, new BigDecimal(20));

        Set<Item> result = solver().solve(input, solverConfig());
        Assertions
                .assertThat(result)
                .isNotNull()
                .hasSize(items.size())
                .isEqualTo(new HashSet<>(items));
    }

    @Test
    public void allItemsFitsToPackageSize() {
        List<Item> items = Lists.newArrayList(
                createItem(1, 5d, 30),
                createItem(2, 5d, 10),
                createItem(3, 5d, 50),
                createItem(4, 5d, 100)
        );

        Package input = new Package(items, new BigDecimal(20));
        Set<Item> result = solver().solve(input, solverConfig());
        Assertions
                .assertThat(result)
                .isNotNull()
                .hasSize(items.size())
                .isEqualTo(new HashSet<>(items));
    }

    @Test
    public void notAllItemsFitsToPackageSize() {
        List<Item> items = Lists.newArrayList(
                createItem(1, 5d, 30),
                createItem(2, 5d, 10),
                createItem(3, 5d, 50),
                createItem(4, 5d, 100)
        );

        Package input = new Package(items, new BigDecimal(15));
        Set<Item> result = solver().solve(input, solverConfig());
        Assertions
                .assertThat(result)
                .isNotNull()
                .hasSize(3)
                .isEqualTo(Sets.newHashSet(
                        createItem(1, 5d, 30),
                        createItem(3, 5d, 50),
                        createItem(4, 5d, 100)
                ));
    }

    @Test
    public void notAllItemsFitsToPackageSizeAndSelectedThatWeightsLessWithSameCost() {
        List<Item> items = Lists.newArrayList(
                createItem(1, 0.4d, 30),
                createItem(2, 0.4d, 10),
                createItem(3, 9.99d, 100),
                createItem(4, 9.75d, 100)
        );

        Package input = new Package(items, new BigDecimal(10.4));
        Set<Item> result = solver().solve(input, solverConfig());
        Assertions
                .assertThat(result)
                .isNotNull()
                .hasSize(2)
                .isEqualTo(Sets.newHashSet(
                        createItem(1, 0.4d, 30),
                        createItem(4, 9.75d, 100)
                ));
    }

    public abstract PackerSolver solver();
}
