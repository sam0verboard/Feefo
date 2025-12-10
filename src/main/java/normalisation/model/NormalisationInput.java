package normalisation.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NormalisationInput {
    @NotBlank(message = "Text is mandatory")
    private String text;

    @NotNull(message = "Type is mandatory")
    private NormalisationInputType type;
}
