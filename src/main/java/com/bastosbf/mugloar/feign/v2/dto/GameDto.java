package com.bastosbf.mugloar.feign.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GameDto extends ScoreDto {

  @JsonProperty("gameId")
  private String id;
  private Long gold;
  private Integer level;
  private Long highScore;

}
