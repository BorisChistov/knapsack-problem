package com.mobiquityinc.packer;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.model.Item;

import java.math.BigDecimal;

public abstract class TestBase {

    protected Item createItem(int id, double weight, double cost) {
        return new Item(
                id,
                new BigDecimal(weight).setScale(solverConfig().getScale(), solverConfig().getRoundingMode()),
                new BigDecimal(cost).setScale(solverConfig().getScale(), solverConfig().getRoundingMode())
        );
    }

    protected BigDecimal toBigDecimal(double value) {
        return new BigDecimal(value).setScale(solverConfig().getScale(), solverConfig().getRoundingMode());
    }

    protected abstract SolverConfiguration solverConfig();
}
