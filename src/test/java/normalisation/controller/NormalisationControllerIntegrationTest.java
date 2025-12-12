package normalisation.controller;

import static normalisation.model.NormalisationInputType.JOB_TITLE;

import normalisation.model.NormalisationInputType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(NormalisationController.class)
@AutoConfigureMockMvc
class NormalisationControllerIntegrationTest {
  @Autowired NormalisationController normalisationController;

  @Autowired private MockMvc mockMvc;

  private final String TEST_ENDPOINT = "/rest/normalise";

  private void postRequestInputTester(String input, ResultMatcher... resultMatchers)
      throws Exception {
    ResultActions resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(TEST_ENDPOINT)
                .content(input)
                .contentType(MediaType.APPLICATION_JSON));

    for (ResultMatcher resultMatcher : resultMatchers) {
      resultActions.andExpect(resultMatcher);
    }
    ;
  }

  private String inputGenerator(String text, NormalisationInputType type) {
    return String.format("{\"text\": \"%s\", \"type\" : \"%s\"}", text, type);
  }

  @Test
  void whenPostRequestToNormaliserAndValidInput_thenCorrectResponse() throws Exception {
    postRequestInputTester(
        inputGenerator("Java engineer", JOB_TITLE),
        MockMvcResultMatchers.status().isOk(),
        MockMvcResultMatchers.content().json("{\"text\":\"Software engineer\"}"));
    postRequestInputTester(
        inputGenerator("C# engineer", JOB_TITLE),
        MockMvcResultMatchers.status().isOk(),
        MockMvcResultMatchers.content().json("{\"text\":\"Software engineer\"}"));
    postRequestInputTester(
        inputGenerator("Accountant", JOB_TITLE),
        MockMvcResultMatchers.status().isOk(),
        MockMvcResultMatchers.content().json("{\"text\":\"Accountant\"}"));
    postRequestInputTester(
        inputGenerator("Chief accountant", JOB_TITLE),
        MockMvcResultMatchers.status().isOk(),
        MockMvcResultMatchers.content().json("{\"text\":\"Accountant\"}"));
  }

  @Test
  void whenPostRequestToNormaliserAndInvalidInput_thenCorrectResponse() throws Exception {
    postRequestInputTester(
        inputGenerator("", JOB_TITLE),
        MockMvcResultMatchers.content().json("{\"text\":\"Text is mandatory\"}"),
        MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void whenPostRequestToNormaliserAndNullInput_thenCorrectResponse() throws Exception {
    postRequestInputTester("{}", MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void whenPostRequestToNormaliserAndMixedCaseInput_thenCorrectResponse() throws Exception {
    postRequestInputTester(
        inputGenerator("SoftWaRE eNgInEer", JOB_TITLE),
        MockMvcResultMatchers.content().json("{\"text\":\"Software engineer\"}"));
  }

  @Test
  void whenPostRequestToNormaliserAndNoMatch_thenCorrectResponse() throws Exception {
    postRequestInputTester(
        inputGenerator("This job does not exist", JOB_TITLE),
        MockMvcResultMatchers.status().isOk(),
        MockMvcResultMatchers.content().json("{\"text\":\"This job does not exist\"}"));
  }

  @Test
  void whenPostRequestToNormaliserAndTypeInvalid_thenCorrectResponse() throws Exception {
    postRequestInputTester(
        inputGenerator("This job does not exist", null),
        MockMvcResultMatchers.status().isBadRequest());
  }
}
