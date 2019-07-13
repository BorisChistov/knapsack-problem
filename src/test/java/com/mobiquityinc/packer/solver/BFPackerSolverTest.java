package com.mobiquityinc.packer.solver;

import com.mobiquityinc.packer.config.SolverConfiguration;

public class BFPackerSolverTest extends PackerSolverTestBase {
    private final BFPackerSolver solver = new BFPackerSolver();
    private final SolverConfiguration solverConfig = new SolverConfiguration();

    @Override
    public SolverConfiguration solverConfig() {
        return solverConfig;
    }

    @Override
    public PackerSolver solver() {
        return solver;
    }
}
