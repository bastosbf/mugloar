package com.bastosbf.mugloar.factory.impl;

import com.bastosbf.mugloar.factory.ItemBuyStrategyFactory;
import com.bastosbf.mugloar.factory.strategy.ItemBuyStrategy;
import com.bastosbf.mugloar.factory.strategy.impl.OneOfEachItemBuyStrategy;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import java.util.List;

public class OneOfEachItemBuyStrategyFactory extends ItemBuyStrategyFactory {

  @Override
  public ItemBuyStrategy create(List<ItemDto> items) {
    return new OneOfEachItemBuyStrategy(items);
  }
}
