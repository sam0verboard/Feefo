package normalisation.service;

import java.io.IOException;
import normalisation.model.JobTitleNormaliser;
import normalisation.model.NormalisationInput;
import normalisation.model.NormalisationOutput;
import normalisation.service.elastic.FuzzyMatcher;
import org.springframework.stereotype.Service;

/** Service to choose which Normaliser implementation should be used for different inputs */
@Service
public class NormalisationService {

  private final FuzzyMatcher fuzzyMatcher;

  public NormalisationService(FuzzyMatcher fuzzyMatcher) {
    this.fuzzyMatcher = fuzzyMatcher;
  }

  public NormalisationOutput normalise(NormalisationInput input) throws IOException {
    if (input == null) {
      throw new UnsupportedOperationException("Normalisation input is null");
    }

    String outputText =
        switch (input.getType()) {
          case JOB_TITLE -> new JobTitleNormaliser(fuzzyMatcher).normalise(input.getText());
          // extend for other types
          default ->
              throw new UnsupportedOperationException(
                  "Normalisation type '%s' is not supported".formatted(input.getType()));
        };
    return new NormalisationOutput(outputText);
  }
}
