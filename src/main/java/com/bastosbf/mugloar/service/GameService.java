package com.bastosbf.mugloar.service;

import static java.util.stream.Collectors.toList;

import com.bastosbf.mugloar.feign.v2.client.DragonsOfMugloarApi;
import com.bastosbf.mugloar.feign.v2.dto.GameDto;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameService {

  @Autowired
  private DragonsOfMugloarApi dragonsOfMugloarApi;

  public GameDto startGame() {
    GameDto game = dragonsOfMugloarApi.start();
    log.debug("Started new game: {}", game);
    return game;
  }

  @Cacheable("items")
  public List<ItemDto> getItemsForGame(String gameId) {
    List<ItemDto> items = dragonsOfMugloarApi.items(gameId).stream()
        .sorted((i1, i2) -> i2.getCost().compareTo(i1.getCost()))
        .collect(toList());
    log.debug("The item list for the game {} is {}", gameId, items);
    return items;
  }
}
