package com.bastosbf.mugloar.utils;

public abstract class Rot13Utils {

  private Rot13Utils() {
  }

  public static String convert(String input) {
    StringBuffer output = new StringBuffer();
    for (char c : input.toCharArray()) {
      if (c >= 'a' && c <= 'm') {
        c += 13;
      } else if (c >= 'A' && c <= 'M') {
        c += 13;
      } else if (c >= 'n' && c <= 'z') {
        c -= 13;
      } else if (c >= 'N' && c <= 'Z') {
        c -= 13;
      }
      output.append(c);
    }
    return output.toString();
  }
}
