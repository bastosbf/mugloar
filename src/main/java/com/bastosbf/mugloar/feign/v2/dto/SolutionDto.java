package com.bastosbf.mugloar.feign.v2.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SolutionDto extends ScoreDto {

  private Boolean success;
  private Long gold;
  private Long highScore;
  private String message;
}
