package com.bastosbf.mugloar.feign.v2.constant;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

import com.bastosbf.mugloar.utils.Rot13Utils;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Base64Utils;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Probability {
  SURE_THING("Sure thing", 1),
  PIECE_OF_CAKE("Piece of cake", 2),
  WALK_IN_THE_PARK("Walk in the park", 3),
  QUITE_LIKELY("Quite likely", 4),
  HMMM("Hmmm....", 5),
  GAMBLE("Gamble", 6),
  RISKY("Risky", 7),
  PLAYING_WITH_FIRE("Playing with fire", 8),
  RATHER_DETRIMENTAL("Rather detrimental", 9),
  SUICIDE_MISSION("Suicide mission", 10),
  IMPOSSIBLE("Impossible", 11),
  UNKNOWN("Unknown", 100);

  private static final Map<String, Probability> CACHE = new TreeMap<>(CASE_INSENSITIVE_ORDER);

  static {
    Arrays.stream(Probability.values()).forEach(p -> {
      CACHE.put(p.text, p);
      CACHE.put(Base64Utils.encodeToString(p.text.getBytes()), p);
      CACHE.put(Rot13Utils.convert(p.text), p);
    });
  }

  private String text;
  private Integer risk;

  @JsonCreator
  public static Probability fromText(String text) {
    return CACHE.getOrDefault(text, UNKNOWN);
  }
}
