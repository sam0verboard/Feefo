package normalisation.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import normalisation.service.elastic.FuzzyMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JobTitleNormaliserTest {

  private JobTitleNormaliser jobTitleNormaliser;

  @Mock private FuzzyMatcher fuzzyMatcher;

  @BeforeEach
  void setup() {
    jobTitleNormaliser = new JobTitleNormaliser(fuzzyMatcher);
  }

  @Test
  void whenInputValid_thenOutputExpected() throws IOException {
    String input = "Sftware engneer";
    String expectedOutput = "Software engineer";

    when(fuzzyMatcher.match(eq(input), anyString(), anyString()))
        .thenReturn(new ArrayList<>(List.of(expectedOutput)));
    String output = jobTitleNormaliser.normalise(input);
    assertEquals(expectedOutput, output);
  }

  @Test
  void whenNoMatch_thenInputReturned() throws IOException {
    String input = "Unmatchable text";
    when(fuzzyMatcher.match(eq(input), anyString(), anyString())).thenReturn(new ArrayList<>());
    String output = jobTitleNormaliser.normalise(input);
    assertEquals(input, output);
  }

  @Test
  void exceptionBubblesUp_whenIOExceptionThrown() throws IOException {
    when(fuzzyMatcher.match(eq("input"), anyString(), anyString()))
        .thenThrow(new IOException("Simulated failure"));

    assertThrows(IOException.class, () -> jobTitleNormaliser.normalise("input"));
  }
}
