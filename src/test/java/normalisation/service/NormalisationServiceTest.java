package normalisation.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import normalisation.model.NormalisationInput;
import normalisation.model.NormalisationInputType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NormalisationServiceTest {
  @Autowired public NormalisationService normalisationService;

  @Test
  void normalise_givesCorrectOutput_whenInputTypeSupported() throws IOException {
    String title = "Software engineer";
    NormalisationInput input = new NormalisationInput(title, NormalisationInputType.JOB_TITLE);
    assertEquals(title, normalisationService.normalise(input).getText());
  }

  @Test
  void normalise_ThrowsException_whenInputTypeSupported() {
    NormalisationInput input =
        new NormalisationInput("test", NormalisationInputType.FUTURE_UNSUPPORTED_TYPE);
    assertThrows(UnsupportedOperationException.class, () -> normalisationService.normalise(input));
  }

  @Test
  void normalise_ThrowsException_whenNullInput() {
    assertThrows(UnsupportedOperationException.class, () -> normalisationService.normalise(null));
  }
}
