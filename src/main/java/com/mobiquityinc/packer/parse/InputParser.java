package com.mobiquityinc.packer.parse;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.model.Package;

public interface InputParser<T> {
    Package parse(T input, SolverConfiguration solverConfiguration);
}
