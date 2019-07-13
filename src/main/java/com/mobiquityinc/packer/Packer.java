package com.mobiquityinc.packer;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.exception.APIException;
import com.mobiquityinc.packer.formatter.ResultFormatter;
import com.mobiquityinc.packer.formatter.StringFormatter;
import com.mobiquityinc.packer.parse.InputParser;
import com.mobiquityinc.packer.parse.StringInputParser;
import com.mobiquityinc.packer.solver.BFPackerSolver;
import com.mobiquityinc.packer.solver.PackerSolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Packer encapsulates main components and perform solution of problem
 *
 */
public class Packer {

    public static InputParser<String> INPUT_PARSER = new StringInputParser();
    public static PackerSolver PACKER_SOLVER = new BFPackerSolver();
    public static ResultFormatter<String> RESULT_FORMATTER = new StringFormatter();

    private final InputParser<String> parser;
    private final PackerSolver solver;
    private final ResultFormatter<String> formatter;
    private final SolverConfiguration configuration;

    public Packer(
            InputParser<String> parser,
            PackerSolver solver,
            ResultFormatter<String> formatter,
            SolverConfiguration configuration
    ) {
        this.parser = parser;
        this.solver = solver;
        this.formatter = formatter;
        this.configuration = configuration;
        if (parser == null) {
            throw new APIException("variable 'INPUT_PARSER' is not configured");
        }

        if (solver == null) {
            throw new APIException("variable 'PACKER_SOLVER' is not configured");
        }

        if (formatter == null) {
            throw new APIException("variable 'RESULT_FORMATTER' is not configured");
        }
    }

    public String packToString(String filePath) throws APIException {
        Path file = Paths.get(filePath);
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new APIException("Illegal file path provided. File doesn't exist or is not regular file");
        }
        try (Stream<String> lineStream = Files.lines(file, StandardCharsets.UTF_8)) {
            return lineStream
                    .map(input -> parser.parse(input, configuration))
                    .map(pack -> solver.solve(pack, configuration))
                    .map(formatter::format)
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new APIException("Failed to read file", e);
        }
    }

    /**
     * Performs parsing file and solving the problem with default settings and brute force algorithm
     * @param filePath - absolute path to file with problems
     * @return formatted string - each line is solution for problem in input file
     * @throws APIException - if input is not available or is not appropriate to specification
     */
    public static String pack(String filePath) throws APIException {
        return new Packer(
                INPUT_PARSER,
                PACKER_SOLVER,
                RESULT_FORMATTER,
                new SolverConfiguration()
        ).packToString(filePath);
    }
}
