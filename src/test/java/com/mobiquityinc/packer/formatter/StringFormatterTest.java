package com.mobiquityinc.packer.formatter;

import com.google.common.collect.Sets;
import com.mobiquityinc.packer.TestBase;
import com.mobiquityinc.packer.config.SolverConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class StringFormatterTest extends TestBase {

    private final StringFormatter formatter = new StringFormatter();
    private final SolverConfiguration solverConfig = new SolverConfiguration();

    @Test
    public void testOneItem() {
        Assertions
                .assertThat(formatter.format(Sets.newHashSet(
                        createItem(1, 5d, 30)
                )))
                .isNotNull()
                .isNotBlank()
                .isEqualTo("1");
    }

    @Test
    public void testManyItems() {
        Assertions
                .assertThat(formatter.format(Sets.newHashSet(
                        createItem(12, 5d, 30),
                        createItem(66, 5d, 10),
                        createItem(33, 5d, 50),
                        createItem(1, 5d, 100)
                )))
                .isNotNull()
                .isNotBlank()
                .isEqualTo("1,12,33,66");
    }

    @Test
    public void testNoItems() {
        Assertions
                .assertThat(formatter.format(Sets.newHashSet()))
                .isNotNull()
                .isNotBlank()
                .isEqualTo("-");
    }

    @Override
    public SolverConfiguration solverConfig() {
        return solverConfig;
    }
}
