package com.mobiquityinc.packer.formatter;

import com.mobiquityinc.packer.model.Item;

import java.util.Set;
import java.util.stream.Collectors;

public class StringFormatter implements ResultFormatter<String> {

    @Override
    public String format(Set<Item> items) {
        if(items.isEmpty()) return "-";
        return items.stream().map(Item::getId).sorted().map(Object::toString).collect(Collectors.joining(","));
    }
}
