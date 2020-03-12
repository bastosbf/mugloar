package com.bastosbf.mugloar.service;

import com.bastosbf.mugloar.factory.strategy.ItemBuyStrategy;
import com.bastosbf.mugloar.feign.v2.client.DragonsOfMugloarApi;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import com.bastosbf.mugloar.feign.v2.dto.ShopDto;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShopServiceTest {

  public static final Long GOLD = Long.MAX_VALUE;
  public static final String GAME_ID = "GAME_ID";
  public static final String ITEM_ID = "ITEM_ID";
  @MockBean
  private DragonsOfMugloarApi dragonsOfMugloarApi;
  @Mock
  private ItemBuyStrategy itemBuyStrategy;
  @Autowired
  private ShopService shopService;
  private ItemDto item;
  private ShopDto shop;

  @BeforeEach
  public void beforeEach() {
    item = new ItemDto();
    Mockito.when(itemBuyStrategy.getItemToBuy(GOLD)).thenReturn(Optional.of(item));
    shop = new ShopDto();
    Mockito.when(dragonsOfMugloarApi.buy(GAME_ID, ITEM_ID)).thenReturn(shop);
  }

  @Test
  public void shouldTryToBuyItem() {
    Optional<ItemDto> item = shopService.getBestItemToBuy(itemBuyStrategy, GOLD);
    Assertions.assertEquals(this.item, item.get());
  }

  @Test
  public void shouldTryToShop() {
    ShopDto shop = shopService.tryToBuyItem(GAME_ID, ITEM_ID);
    Assertions.assertEquals(this.shop, shop);
  }

}