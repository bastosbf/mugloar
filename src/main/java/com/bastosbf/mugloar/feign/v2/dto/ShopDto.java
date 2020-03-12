package com.bastosbf.mugloar.feign.v2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShopDto extends TurnDto {

  @JsonProperty("shoppingSuccess")
  private Boolean success;
  private Long gold;
  private Integer level;
}
