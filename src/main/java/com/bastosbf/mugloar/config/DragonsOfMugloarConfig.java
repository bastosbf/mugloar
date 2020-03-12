package com.bastosbf.mugloar.config;

import static java.util.Collections.singletonList;

import com.bastosbf.mugloar.daemon.DragonsOfMugloarDaemon;
import com.bastosbf.mugloar.factory.ItemBuyStrategyFactory;
import com.bastosbf.mugloar.factory.impl.OneOfEachItemBuyStrategyFactory;
import com.bastosbf.mugloar.service.GameService;
import com.bastosbf.mugloar.service.QuestionService;
import com.bastosbf.mugloar.service.ShopService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class DragonsOfMugloarConfig {

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(singletonList(new ConcurrentMapCache("items")));
    return cacheManager;
  }

  @Bean
  public ItemBuyStrategyFactory itemBuyStrategyFactory() {
    return new OneOfEachItemBuyStrategyFactory();
  }

  @Bean
  public DragonsOfMugloarDaemon gameDaemon(GameService gameService, QuestionService questionService,
      ShopService shopService, ItemBuyStrategyFactory itemBuyStrategyFactory) {
    return new DragonsOfMugloarDaemon(gameService, questionService, shopService,
        itemBuyStrategyFactory);
  }
}