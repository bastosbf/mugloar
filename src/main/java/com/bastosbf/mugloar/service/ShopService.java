package com.bastosbf.mugloar.service;

import com.bastosbf.mugloar.factory.strategy.ItemBuyStrategy;
import com.bastosbf.mugloar.feign.v2.client.DragonsOfMugloarApi;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import com.bastosbf.mugloar.feign.v2.dto.ShopDto;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShopService {

  @Autowired
  private DragonsOfMugloarApi dragonsOfMugloarApi;

  public Optional<ItemDto> getBestItemToBuy(ItemBuyStrategy itemBuyStrategy, Long gold) {
    Optional<ItemDto> item = itemBuyStrategy.getItemToBuy(gold);
    log.debug("The best item to buy with gold {} is {}", gold, item);
    return item;
  }

  public ShopDto tryToBuyItem(String gameId, String itemId) {
    ShopDto shop = dragonsOfMugloarApi.buy(gameId, itemId);
    log.debug("Tried to buy the item {} in the shop {}", itemId, shop);
    return shop;
  }

}
