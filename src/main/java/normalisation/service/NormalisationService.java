package normalisation.service;

import normalisation.model.JobTitleNormaliser;
import normalisation.model.NormalisationInput;
import normalisation.model.NormalisationOutput;
import org.springframework.stereotype.Service;

@Service
public class NormalisationService {
    public NormalisationOutput normalise(NormalisationInput input) {
        String outputText = switch (input.getType()) {
            case JOB_TITLE -> new JobTitleNormaliser().normalise(input.getText());
            // extend for other types
            default -> throw new UnsupportedOperationException(
                    "Normalisation type '%s' is not supported".formatted(input.getType())
            );
        };
        return new NormalisationOutput(outputText);
    }
}
