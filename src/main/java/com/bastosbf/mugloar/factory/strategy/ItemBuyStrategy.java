package com.bastosbf.mugloar.factory.strategy;

import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import java.util.Optional;

public interface ItemBuyStrategy {

  Optional<ItemDto> getItemToBuy(Long gold);
}
