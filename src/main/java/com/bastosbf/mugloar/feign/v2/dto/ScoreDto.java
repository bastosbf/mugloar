package com.bastosbf.mugloar.feign.v2.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ScoreDto extends TurnDto {

  private Long score;
}
