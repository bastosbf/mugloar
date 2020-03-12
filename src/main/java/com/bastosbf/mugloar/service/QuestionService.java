package com.bastosbf.mugloar.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import com.bastosbf.mugloar.feign.v2.client.DragonsOfMugloarApi;
import com.bastosbf.mugloar.feign.v2.constant.Probability;
import com.bastosbf.mugloar.feign.v2.dto.QuestionDto;
import com.bastosbf.mugloar.feign.v2.dto.SolutionDto;
import com.bastosbf.mugloar.utils.ProbabilityUtils;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

@Slf4j
@Service
public class QuestionService {

  //Solution for apache commons code bug: https://issues.apache.org/jira/browse/CODEC-263
  //https://github.com/Netflix/msl/blob/master/core/src/main/java/com/netflix/msl/util/Base64.java
  private static final Pattern BASE64_PATTERN = Pattern
      .compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");

  @Value("${com.bastosbf.mugloar.livesForBraveMode:5}")
  private Integer livesForBraveMode;

  @Autowired
  private DragonsOfMugloarApi dragonsOfMugloarApi;

  public List<QuestionDto> getBestQuestionsToSolve(String gameId, Integer lives) {
    List<QuestionDto> questions = dragonsOfMugloarApi.questions(gameId).stream()
        .sorted((q1, q2) -> {
          Integer r1 = q1.getProbability().getRisk();
          Integer r2 = q2.getProbability().getRisk();
          Integer x1 = q1.getExpiresIn();
          Integer x2 = q2.getExpiresIn();

          Integer r = r1.compareTo(r2);
          return r == 0 ? x1.compareTo(x2) : r;
        })
        .peek(q -> {
          if (BASE64_PATTERN.matcher(q.getMessage()).matches()) {
            q.setId(new String(Base64Utils.decodeFromString(q.getId()), UTF_8));
            q.setMessage(new String(Base64Utils.decodeFromString(q.getMessage()), UTF_8));
          }
        })
        .collect(toList());
    Probability probability =
        lives >= livesForBraveMode ? ProbabilityUtils.findBestProbability(lives)
            : questions.get(0).getProbability();
    return filterBestQuestionsToSolve(questions, probability);
  }

  private List<QuestionDto> filterBestQuestionsToSolve(List<QuestionDto> questions,
      Probability probability) {
    List<QuestionDto> filteredQuestions = questions.stream()
        .filter(q -> q.getProbability().getRisk() <= probability.getRisk())
        .collect(toList());
    if (filteredQuestions.isEmpty()) {
      Probability newProbability = ProbabilityUtils.findBestProbability(probability.getRisk() + 1);
      log.debug("Question list for for the probability {} is empty, probability changed to: {}",
          probability, newProbability);
      return filterBestQuestionsToSolve(questions, newProbability);
    }
    log.debug("Question list for the probability {} is {}", probability, filteredQuestions);
    return filteredQuestions;
  }

  public SolutionDto tryToSolveQuestion(String gameId, String questionId) {
    SolutionDto solution = dragonsOfMugloarApi.solve(gameId, questionId);
    log.debug("The solution for the question {} is {}", questionId, solution);
    return solution;
  }
}
