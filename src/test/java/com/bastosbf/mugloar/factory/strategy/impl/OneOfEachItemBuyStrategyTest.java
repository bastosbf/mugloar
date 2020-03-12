package com.bastosbf.mugloar.factory.strategy.impl;

import com.bastosbf.mugloar.feign.v2.client.DragonsOfMugloarApi;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OneOfEachItemBuyStrategyTest {

  public static final Integer ITEM_MAX_COST = Integer.MAX_VALUE;
  public static final Integer ITEM_MIN_COST = Integer.MIN_VALUE;

  @MockBean
  private DragonsOfMugloarApi dragonsOfMugloarApi;
  private OneOfEachItemBuyStrategy oneOfEachItemBuyStrategy;
  private ItemDto mostExpensive;
  private ItemDto cheapest;

  @BeforeEach
  private void beforeEach() {
    List<ItemDto> items = new ArrayList<>();
    mostExpensive = new ItemDto();
    mostExpensive.setCost(ITEM_MAX_COST);
    items.add(mostExpensive);

    cheapest = new ItemDto();
    cheapest.setCost(ITEM_MIN_COST);
    items.add(cheapest);

    oneOfEachItemBuyStrategy = new OneOfEachItemBuyStrategy(items);
  }

  @Test
  public void shouldGetMostExpensiveItemToBuy() {
    Optional<ItemDto> item = oneOfEachItemBuyStrategy.getItemToBuy(ITEM_MAX_COST + 1L);
    Assertions.assertEquals(mostExpensive, item.get());
  }

  @Test
  public void shouldGetCheapestItemToBuy() {
    Optional<ItemDto> item = oneOfEachItemBuyStrategy.getItemToBuy(ITEM_MIN_COST.longValue());
    Assertions.assertEquals(cheapest, item.get());
  }

  @Test
  public void shouldGetCheapestItemToBuyBecauseOfTheQuantityEvenWhenWithWEnoughGold() {
    oneOfEachItemBuyStrategy.getItemToBuy(ITEM_MAX_COST + 1L);
    Optional<ItemDto> item = oneOfEachItemBuyStrategy.getItemToBuy(ITEM_MAX_COST + 1L);
    Assertions.assertEquals(cheapest, item.get());
  }

  @Test
  public void shouldNotBeAbleToBuyAnyItem() {
    Optional<ItemDto> item = oneOfEachItemBuyStrategy.getItemToBuy(ITEM_MIN_COST - 1L);
    Assertions.assertFalse(item.isPresent());
  }

}