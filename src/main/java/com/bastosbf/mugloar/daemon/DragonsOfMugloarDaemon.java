package com.bastosbf.mugloar.daemon;

import com.bastosbf.mugloar.factory.ItemBuyStrategyFactory;
import com.bastosbf.mugloar.factory.strategy.ItemBuyStrategy;
import com.bastosbf.mugloar.feign.v2.dto.GameDto;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import com.bastosbf.mugloar.feign.v2.dto.QuestionDto;
import com.bastosbf.mugloar.feign.v2.dto.ScoreDto;
import com.bastosbf.mugloar.feign.v2.dto.ShopDto;
import com.bastosbf.mugloar.feign.v2.dto.SolutionDto;
import com.bastosbf.mugloar.feign.v2.dto.TurnDto;
import com.bastosbf.mugloar.service.GameService;
import com.bastosbf.mugloar.service.QuestionService;
import com.bastosbf.mugloar.service.ShopService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DragonsOfMugloarDaemon {

  private GameService gameService;
  private QuestionService questionService;
  private ShopService shopService;
  private ItemBuyStrategyFactory itemBuyStrategyFactory;

  private GameDto game;
  private AtomicInteger lives;
  private AtomicInteger turns;
  private AtomicLong score;
  private ItemBuyStrategy itemBuyStrategy;

  private Thread process;

  public DragonsOfMugloarDaemon(GameService gameService, QuestionService questionService,
      ShopService shopService, ItemBuyStrategyFactory itemBuyStrategyFactory) {
    this.gameService = gameService;
    this.questionService = questionService;
    this.shopService = shopService;
    this.itemBuyStrategyFactory = itemBuyStrategyFactory;

  }

  public void init() {
    game = gameService.startGame();
    lives = new AtomicInteger(game.getLives());
    turns = new AtomicInteger(game.getTurn());
    score = new AtomicLong(game.getScore());

    List<ItemDto> items = gameService.getItemsForGame(game.getId());
    itemBuyStrategy = itemBuyStrategyFactory.create(items);
  }

  public void start() {
    Objects.requireNonNull(game);
    Objects.requireNonNull(lives);
    Objects.requireNonNull(turns);
    Objects.requireNonNull(itemBuyStrategy);

    this.process = new Thread(() -> {
      do {
        List<QuestionDto> questions = questionService
            .getBestQuestionsToSolve(game.getId(), lives.get());
        Integer questionsTurn = turns.get();
        for (QuestionDto question : questions) {
          if ((questionsTurn + question.getExpiresIn()) <= turns.get()) {
            continue;
          }
          SolutionDto solution = questionService.tryToSolveQuestion(game.getId(), question.getId());
          updateScore(solution);
          if (solution.getSuccess()) {
            Optional<ItemDto> item = shopService
                .getBestItemToBuy(itemBuyStrategy, solution.getGold());
            item.ifPresent(i -> {
              ShopDto shop = shopService.tryToBuyItem(game.getId(), i.getId());
              updateTurn(shop);
            });
          } else if (lives.get() == 0) {
            break;
          }
        }
      } while (lives.get() > 0);
      log.info("Game Over with score: {}", score);
    });
    this.process.start();
  }

  public void stop() {
    this.process.interrupt();
  }

  private void updateTurn(TurnDto turn) {
    this.lives.set(turn.getLives());
    this.turns.set(turn.getTurn());
  }

  public void updateScore(ScoreDto score) {
    this.score.set(score.getScore());
    updateTurn(score);
  }

}
