package com.bastosbf.mugloar.feign.v2.client;

import com.bastosbf.mugloar.feign.v2.dto.GameDto;
import com.bastosbf.mugloar.feign.v2.dto.ItemDto;
import com.bastosbf.mugloar.feign.v2.dto.QuestionDto;
import com.bastosbf.mugloar.feign.v2.dto.ReputationDto;
import com.bastosbf.mugloar.feign.v2.dto.ShopDto;
import com.bastosbf.mugloar.feign.v2.dto.SolutionDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("dragons-of-mugloar-api")
public interface DragonsOfMugloarApi {

  @PostMapping("/api/v2/game/start")
  GameDto start();

  @PostMapping("/api/v2/{gameId}/investigate/reputation")
  ReputationDto reputation(@PathVariable String gameId);

  @GetMapping("/api/v2/{gameId}/messages")
  List<QuestionDto> questions(@PathVariable String gameId);

  @PostMapping("/api/v2/{gameId}/solve/{adId}")
  SolutionDto solve(@PathVariable String gameId, @PathVariable String adId);

  @GetMapping("/api/v2/{gameId}/shop")
  List<ItemDto> items(@PathVariable String gameId);

  @PostMapping("/api/v2/{gameId}/shop/buy/{itemId}")
  ShopDto buy(@PathVariable String gameId, @PathVariable String itemId);
}
