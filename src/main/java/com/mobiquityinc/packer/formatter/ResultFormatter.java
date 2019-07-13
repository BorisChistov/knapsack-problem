package com.mobiquityinc.packer.formatter;

import com.mobiquityinc.packer.model.Item;

import java.util.Set;

public interface ResultFormatter<T> {
    T format(Set<Item> items);
}
