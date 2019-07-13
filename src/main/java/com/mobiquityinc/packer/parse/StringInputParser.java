package com.mobiquityinc.packer.parse;

import com.mobiquityinc.packer.config.SolverConfiguration;
import com.mobiquityinc.packer.exception.APIException;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringInputParser implements InputParser<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringInputParser.class);
    private static final Pattern inputPattern = Pattern.compile(
            "\\d+\\s*:\\s*(\\((?<id>\\d+),(?<weight>\\d+.?\\d+),\\p{Sc}(?<cost>\\d+.?\\d*)\\)\\s*)*"
    );
    private static final Pattern itemPattern = Pattern.compile(
            "\\((?<id>\\d+),(?<weight>\\d+.?\\d+),\\p{Sc}(?<cost>\\d+.?\\d*)\\)"
    );


    // TODO: Should I throw exceptions on invalid input or just ignore it
    @Override
    public Package parse(String input, SolverConfiguration config) {
        if (!inputPattern.matcher(input).matches()) {
            throw new APIException("Invalid input format. Expecting: 'weight : (id, weight, cost)..., got: " + input);
        }
        LOGGER.trace("Parsing input: {}", input);
        String[] parts = input.replaceAll("\\s", "").split(":");
        BigDecimal weight = parseDouble(parts[0], config);
        if (config.getMaxPackageWeight().compareTo(weight) < 0) {
            throw new APIException("Invalid package weight provided, exceeded max: " + config.getMaxPackageWeight());
        }
        List<Item> items = parts.length == 2 ? parseItems(parts[1], config) : Collections.emptyList();
        if (config.getMaxItems() < items.size()) {
            throw new APIException("Items size exceeded max: " + config.getMaxItems());
        }
        LOGGER.trace("Weight: {}, Items: {}", weight, items);
        return new Package(items, weight);
    }

    private List<Item> parseItems(String input, SolverConfiguration config) {
        List<Item> result = new ArrayList<>();
        Matcher itemMatcher = itemPattern.matcher(input);
        while (itemMatcher.find()) {
            Item item = new Item(
                    parseInteger(itemMatcher.group("id")),
                    parseDouble(itemMatcher.group("weight"), config),
                    parseDouble(itemMatcher.group("cost"), config)
            );
            LOGGER.trace("id: {}, weight: {}, cost: {}", item.getId(), item.getWeight(), item.getCost());
            if (item.getWeight().compareTo(config.getMaxItemWeight()) > 0) {
                throw new APIException("Invalid item weight, exceeded max: " + config.getMaxItemWeight());
            }
            if (item.getCost().compareTo(config.getMaxCost()) > 0) {
                throw new APIException("Invalid item cost, exceeded max: " + config.getMaxItemWeight());
            }
            result.add(item);
        }
        return result;
    }

    private int parseInteger(String input) {
        return Integer.parseInt(input);
    }

    private BigDecimal parseDouble(String input, SolverConfiguration config) {
        try {
            return new BigDecimal(Double.parseDouble(input)).setScale(config.getScale(), config.getRoundingMode());
        } catch (NumberFormatException e) {
            throw new APIException("Failed to parse input", e);
        }
    }
}
