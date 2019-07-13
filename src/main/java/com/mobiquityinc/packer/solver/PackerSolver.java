package com.mobiquityinc.packer.solver;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Package;

import java.util.Set;

public interface PackerSolver {
    Set<Item> solve(Package input, SolverConfiguration config);
}
