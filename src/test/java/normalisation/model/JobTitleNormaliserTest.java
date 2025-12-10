package normalisation.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JobTitleNormaliserTest {
    @Test
    public void whenInputTypeX_thenOutputY() {
        String input = "";
        String output = new JobTitleNormaliser().normalise(input);
        String expectedOutput = "";
        assertEquals(expectedOutput, output);
    }
}
