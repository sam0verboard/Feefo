package normalisation.controller;

import normalisation.model.NormalisationInputType;
import org.hamcrest.core.Is;
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

import static normalisation.model.NormalisationInputType.JOB_TITLE;

@WebMvcTest(NormalisationController.class)
@AutoConfigureMockMvc
public class NormalisationControllerIntegrationTest {
    @Autowired
    NormalisationController normalisationController;

    @Autowired
    private MockMvc mockMvc;

    private final String TEST_ENDPOINT = "/rest/normalise";

    private void postRequestInputTester(String input, ResultMatcher... resultMatchers) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(TEST_ENDPOINT)
                        .content(input)
                        .contentType(MediaType.APPLICATION_JSON));

       for(ResultMatcher resultMatcher : resultMatchers) {
                resultActions.andExpect(resultMatcher);
        };
    }

    private String inputGenerator(String text, NormalisationInputType type) {
        return String.format("{\"text\": %s, \"type\" : %s}", text, type);
    }


    @Test
    public void whenPostRequestToNormaliserAndValidInput_thenCorrectResponse() throws Exception {
        postRequestInputTester(
                inputGenerator("Java engineer", JOB_TITLE),
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void whenPostRequestToNormaliserAndInvalidInput_thenCorrectResponse() throws Exception {
        postRequestInputTester(
                inputGenerator("", JOB_TITLE),
                MockMvcResultMatchers.jsonPath("$.text", Is.is("Text is mandatory")),
                MockMvcResultMatchers.jsonPath("$.type", Is.is("Type is mandatory")),
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

//    @Test
//    public void whenPostRequestToNormaliserAndNullInput_thenCorrectResponse() throws Exception {
//        postRequestInputTester(
//                "{}",
//                expected response
//        );
//    }
//    @Test
//    public void whenPostRequestToNormaliserAndMixedCaseInput_thenCorrectResponse() throws Exception {
//        postRequestInputTester(
//                inputGenerator("JaVa eNgInEer", JOB_TITLE),
//                expected response
//        );
//    }
//
//    @Test
//    public void whenPostRequestToNormaliserAndWhitespaceInput_thenCorrectResponse() throws Exception {
//        postRequestInputTester(
//                inputGenerator("JaVa eNgInEer", JOB_TITLE),
//                expected response
//        );
//    }
//
//    @Test
//    public void whenPostRequestToNormaliserAndNoMatch_thenCorrectResponse() throws Exception {
//        postRequestInputTester(
//                inputGenerator("This job does not exist", JOB_TITLE),
//                expected response
//        );
//    }
//
//    @Test
//    public void whenPostRequestToNormaliserAndTypeInvalid_thenCorrectResponse() throws Exception {
//        postRequestInputTester(
//                {"type": "test"},
//                expected response
//        );
//    }
}
