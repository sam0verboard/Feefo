package normalisation.service;

import normalisation.model.NormalisationInput;
import normalisation.model.NormalisationInputType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

public class NormalisationServiceTest {
    @Autowired
    public NormalisationService normalisationService;

    @Test
    public void whenInputTypeSupported_thenCorrectOutput() {
        NormalisationInput input = new NormalisationInput("test", NormalisationInputType.JOB_TITLE);
        assertEquals("Expected text", normalisationService.normalise(input).getText());
    }

    @Test
    public void whenInputTypeSupported_thenThrowException() {
        NormalisationInput input = new NormalisationInput("test", NormalisationInputType.FUTURE_UNSUPPORTED_TYPE);
        assertThrows(UnsupportedOperationException.class,
                () -> normalisationService.normalise(input));
    }
}
