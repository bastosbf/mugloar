package com.bastosbf.mugloar.utils;

import static com.bastosbf.mugloar.feign.v2.constant.Probability.UNKNOWN;

import com.bastosbf.mugloar.feign.v2.constant.Probability;

public abstract class ProbabilityUtils {

  private ProbabilityUtils() {
  }

  public static Probability findBestProbability(Integer lives) {
    for (Probability p : Probability.values()) {
      if (p.getRisk().equals(lives)) {
        return p;
      }
    }
    return UNKNOWN;
  }

}
