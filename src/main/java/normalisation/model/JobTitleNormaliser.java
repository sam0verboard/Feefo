package normalisation.model;

public class JobTitleNormaliser implements Normaliser {
    public String normalise(String input) {
        return "This job title has been normalised: " + input;
    }
}
