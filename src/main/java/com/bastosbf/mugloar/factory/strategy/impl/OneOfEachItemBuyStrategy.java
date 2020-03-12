package com.bastosbf.mugloar.factory.strategy.impl;

import static java.util.stream.Collectors.toMap;

import com.bastosbf.mugloar.factory.strategy.ItemBuyStrategy;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

public class OneOfEachItemBuyStrategy implements ItemBuyStrategy {

  private Map<ItemDto, Integer> items;

  public OneOfEachItemBuyStrategy(List<ItemDto> items) {
    this.items = items.stream()
        .sorted((i1, i2) -> i2.getCost().compareTo(i1.getCost()))
        .collect(toMap(Function.identity(), v -> 0, (e1, e2) -> e1, LinkedHashMap::new));
    ;
  }

  @Override
  public Optional<ItemDto> getItemToBuy(Long gold) {
    Optional<ItemDto> item = items.entrySet()
        .stream()
        .sorted((i1, i2) -> {
          Integer v1 = i1.getValue();
          Integer v2 = i2.getValue();
          Integer k1 = i1.getKey().getCost();
          Integer k2 = i2.getKey().getCost();

          Integer r = v1.compareTo(v2);
          return r == 0 ? k2.compareTo(k1) : r;
        })
        .map(Entry::getKey)
        .filter(i -> i.getCost() <= gold)
        .findFirst();
    item.ifPresent(i -> items.computeIfPresent(i, (k, v) -> ++v));
    return item;
  }
}
