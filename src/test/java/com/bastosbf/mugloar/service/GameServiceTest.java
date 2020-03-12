package com.bastosbf.mugloar.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bastosbf.mugloar.feign.v2.client.DragonsOfMugloarApi;
import com.bastosbf.mugloar.feign.v2.dto.GameDto;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GameServiceTest {

  public static final String GAME_ID = "GAME_ID";
  public static final Integer ITEM_MAX_COST = Integer.MAX_VALUE;
  public static final Integer ITEM_MIN_COST = Integer.MIN_VALUE;

  @MockBean
  private DragonsOfMugloarApi dragonsOfMugloarApi;
  @Autowired
  private GameService gameService;

  @BeforeEach
  public void beforeEach() {
    GameDto gameDto = new GameDto();
    gameDto.setId(GAME_ID);

    List<ItemDto> items = new ArrayList<>();
    ItemDto item1 = new ItemDto();
    item1.setCost(ITEM_MAX_COST);
    items.add(item1);

    ItemDto item2 = new ItemDto();
    item2.setCost(ITEM_MIN_COST);
    items.add(item2);

    Mockito.when(dragonsOfMugloarApi.start()).thenReturn(gameDto);
    Mockito.when(dragonsOfMugloarApi.items(GAME_ID)).thenReturn(items);
  }

  @Test
  public void shouldStartGame() {
    GameDto game = gameService.startGame();
    assertEquals(GAME_ID, game.getId());
  }

  @Test
  public void shouldGetSortedListOfItems() {
    List<ItemDto> items = gameService.getItemsForGame(GAME_ID);
    List<Integer> costs = items.stream().map(ItemDto::getCost).collect(Collectors.toList());
    Assertions.assertEquals(costs.stream()
        .sorted(Comparator.comparingInt(Integer::intValue).reversed())
        .collect(Collectors.toList()), costs);
  }

  @Test
  public void shouldGetListOfItemsFromCache() {
    gameService.getItemsForGame(GAME_ID);
    gameService.getItemsForGame(GAME_ID);
    Mockito.verify(dragonsOfMugloarApi, Mockito.times(1)).items(GAME_ID);
  }

}