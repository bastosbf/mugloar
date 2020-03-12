package com.bastosbf.mugloar.factory;

import com.bastosbf.mugloar.factory.strategy.ItemBuyStrategy;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import java.util.List;

public abstract class ItemBuyStrategyFactory {

  public abstract ItemBuyStrategy create(List<ItemDto> items);
}
