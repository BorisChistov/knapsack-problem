package com.mobiquityinc.packer.solver;

import com.mobiquityinc.packer.config.SolverConfiguration;

public class DPPackerSolverTest extends PackerSolverTestBase {
    private final PackerSolver solver = new DPPackerSolver();
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
