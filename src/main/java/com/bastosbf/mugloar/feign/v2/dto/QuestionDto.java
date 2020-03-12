package com.bastosbf.mugloar.feign.v2.dto;

import com.bastosbf.mugloar.feign.v2.constant.Probability;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionDto {

  @JsonProperty("adId")
  private String id;
  private String message;
  private String reward;
  private Integer expiresIn;
  private Probability probability;
}
