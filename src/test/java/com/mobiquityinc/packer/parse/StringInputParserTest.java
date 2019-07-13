package com.mobiquityinc.packer.parse;

import com.mobiquityinc.packer.TestBase;
import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.exception.APIException;
import com.mobiquityinc.packer.model.Package;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class StringInputParserTest extends TestBase {

    private StringInputParser parser = new StringInputParser();
    private SolverConfiguration solverConfiguration = new SolverConfiguration();

    @Test
    public void parsePackageWithOneItem() {
        String inputValue = "8 : (1,15.3,€34.2)";
        Package result = parser.parse(inputValue, solverConfiguration);
        Assertions.assertThat(result)
                .isNotNull()
                .extracting(Package::getWeight).isEqualTo(toBigDecimal(8));
        Assertions
                .assertThat(result.getItems())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(createItem(1, 15.3, 34.2));
    }

    @Test
    public void parsePackageWithSeveralItems() {
        String inputValue = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3)";
        Package result = parser.parse(inputValue, new SolverConfiguration());
        Assertions.assertThat(result)
                .isNotNull()
                .extracting(Package::getWeight).isEqualTo(toBigDecimal(81));
        Assertions
                .assertThat(result.getItems())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(
                        createItem(1, 53.38, 45),
                        createItem(2, 88.62, 98),
                        createItem(3, 78.48, 3)
                );
    }

    @Test
    public void parsePackageWithoutItems() {
        String inputValue = "81 : ";
        Package result = parser.parse(inputValue, new SolverConfiguration());
        Assertions
                .assertThat(result)
                .isNotNull()
                .extracting(Package::getWeight).isEqualTo(toBigDecimal(81));

        Assertions
                .assertThat(result.getItems())
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void parseBadInput() {
        String inputValue = "81 ; (1,53.38,€45) (2,88.62,€98) (3,78.48,€3)";
        Assertions
                .assertThatThrownBy(() -> parser.parse(inputValue, new SolverConfiguration()))
                .isExactlyInstanceOf(APIException.class);
    }

    @Test
    public void testIfPackageWeightMoreThenConfigured() {
        String inputValue = "181 ; (1,53.38,€45) (2,88.62,€98) (3,78.48,€3)";
        Assertions
                .assertThatThrownBy(() -> parser.parse(inputValue, solverConfiguration))
                .isExactlyInstanceOf(APIException.class);
    }

    @Test
    public void testIfItemWeightMoreThenConfigured() {
        String inputValue = "12 ; (1,53.38,€45) (2,181.62,€98) (3,78.48,€3)";
        Assertions
                .assertThatThrownBy(() -> parser.parse(inputValue, solverConfiguration))
                .isExactlyInstanceOf(APIException.class);
    }

    @Test
    public void testIfItemCostMoreThenConfigured() {
        String inputValue = "12 ; (1,53.38,€45) (2,81.62,€198) (3,78.48,€3)";
        Assertions
                .assertThatThrownBy(() -> parser.parse(inputValue, solverConfiguration))
                .isExactlyInstanceOf(APIException.class);
    }

    @Test
    public void testIfItemCountMoreThenConfigured() {
        String inputValue = "12 ; (1,53.38,€45) (2,81.62,€198) (3,78.48,€3) (1,53.38,€45) (2,81.62,€98)" +
                " (3,78.48,€3) (1,53.38,€45) (2,81.62,€98) (1,53.38,€45) (2,81.62,€98)" +
                " (3,78.48,€3) (1,53.38,€45) (2,81.62,€98) (3,78.48,€3) (1,53.38,€45) (2,81.62,€98) (3,78.48,€3)";
        Assertions
                .assertThatThrownBy(() -> parser.parse(inputValue, solverConfiguration))
                .isExactlyInstanceOf(APIException.class);
    }

    @Override
    public SolverConfiguration solverConfig() {
        return solverConfiguration;
    }
}
