package com.bastosbf.mugloar.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.bastosbf.mugloar.feign.v2.client.DragonsOfMugloarApi;
import com.bastosbf.mugloar.feign.v2.constant.Probability;
import com.bastosbf.mugloar.feign.v2.dto.QuestionDto;
import com.bastosbf.mugloar.feign.v2.dto.SolutionDto;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Base64Utils;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuestionServiceTest {

  public static final String GAME_ID = "GAME_ID";
  public static final String BASE64_ID = "BASE64_ID";
  public static final String BASE64_MESSAGE = "BASE64_MESSAGE";
  public static final String NON_BASE64_MESSAGE = "NON_BASE64_MESSAGE";
  public static final Integer LIVES = 1;
  @MockBean
  private DragonsOfMugloarApi dragonsOfMugloarApi;
  @Autowired
  private QuestionService questionService;
  private SolutionDto solution;
  private QuestionDto easiestQuestion;
  private QuestionDto easierQuestion1;
  private QuestionDto easierQuestion2;
  private QuestionDto harderQuestion;

  @BeforeEach
  public void beforeEach() {
    solution = new SolutionDto();
    Mockito.when(dragonsOfMugloarApi.solve(Mockito.eq(GAME_ID), Mockito.anyString()))
        .thenReturn(solution);
    easiestQuestion = new QuestionDto();
    easiestQuestion.setProbability(Probability.SURE_THING);
    easiestQuestion.setExpiresIn(4);
    easiestQuestion.setMessage(NON_BASE64_MESSAGE);

    easierQuestion1 = new QuestionDto();
    easierQuestion1.setProbability(Probability.WALK_IN_THE_PARK);
    easierQuestion1.setExpiresIn(3);
    easierQuestion1.setId(Base64Utils.encodeToString(BASE64_ID.getBytes(UTF_8)));
    easierQuestion1.setMessage(Base64Utils.encodeToString(BASE64_MESSAGE.getBytes(UTF_8)));

    easierQuestion2 = new QuestionDto();
    easierQuestion2.setProbability(Probability.WALK_IN_THE_PARK);
    easierQuestion2.setExpiresIn(2);
    easierQuestion2.setMessage(NON_BASE64_MESSAGE);

    harderQuestion = new QuestionDto();
    harderQuestion.setProbability(Probability.GAMBLE);
    harderQuestion.setExpiresIn(1);
    harderQuestion.setMessage(NON_BASE64_MESSAGE);
  }

  @Test
  public void shouldGetEasiestQuestionsFirst() {
    Mockito.when(dragonsOfMugloarApi.questions(GAME_ID)).thenReturn(
        Arrays.asList(easiestQuestion, easierQuestion1, easierQuestion2, harderQuestion));

    List<QuestionDto> questions = questionService.getBestQuestionsToSolve(GAME_ID, LIVES);
    Assertions.assertEquals(easiestQuestion, questions.get(0));
  }

  @Test
  public void shouldGetEasiestMessagesThatExpireFirst() {
    Mockito.when(dragonsOfMugloarApi.questions(GAME_ID)).thenReturn(
        Arrays.asList(easierQuestion1, easierQuestion2, harderQuestion));
    List<QuestionDto> questions = questionService.getBestQuestionsToSolve(GAME_ID, LIVES);
    Assertions.assertEquals(easierQuestion2, questions.get(0));
  }

  @Test
  public void shouldDecodeBase64Messages() {
    Mockito.when(dragonsOfMugloarApi.questions(GAME_ID)).thenReturn(Arrays.asList(easierQuestion1));
    List<QuestionDto> questions = questionService.getBestQuestionsToSolve(GAME_ID, LIVES);
    Assertions.assertEquals(BASE64_ID, questions.get(0).getId());
  }

  @Test
  public void shouldTryToSolveTheQuestion() {
    SolutionDto solution = questionService.tryToSolveQuestion(GAME_ID, RandomStringUtils.random(1));
    Assertions.assertEquals(this.solution, solution);
  }
}