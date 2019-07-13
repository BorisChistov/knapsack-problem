package com.mobiquityinc.packer.config;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SolverConfiguration {

    private final int maxItems;
    private final int scale;
    private final RoundingMode roundingMode;
    private final BigDecimal maxCost;
    private final BigDecimal maxItemWeight;
    private final BigDecimal maxPackageWeight;


    public SolverConfiguration(
            int maxItems,
            int scale,
            RoundingMode roundingMode,
            BigDecimal maxCost,
            BigDecimal maxItemWeight,
            BigDecimal maxPackageWeight
    ) {
        this.maxItems = maxItems;
        this.scale = scale;
        this.roundingMode = roundingMode;
        this.maxCost = maxCost.setScale(scale, roundingMode);
        this.maxItemWeight = maxItemWeight.setScale(scale, roundingMode);
        this.maxPackageWeight = maxPackageWeight.setScale(scale, roundingMode);
    }

    public SolverConfiguration() {
        this(15, 2, RoundingMode.HALF_UP, new BigDecimal(100), new BigDecimal(100), new BigDecimal(100));
    }

    public int getMaxItems() {
        return maxItems;
    }

    public int getScale() {
        return scale;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    public BigDecimal getMaxCost() {
        return maxCost;
    }

    public BigDecimal getMaxItemWeight() {
        return maxItemWeight;
    }

    public BigDecimal getMaxPackageWeight() {
        return maxPackageWeight;
    }
}
