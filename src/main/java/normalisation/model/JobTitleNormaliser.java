package normalisation.model;

import java.io.IOException;
import java.util.List;
import normalisation.service.elastic.FuzzyMatcher;

/**
 * Normaliser implementation for job titles. Uses elasticsearch fuzzy matching. Defaults to input if
 * match is not found in index Exceptions bubble up.
 */
public class JobTitleNormaliser implements Normaliser {

  private final FuzzyMatcher matcher;
  private static final String INDEX = "job_titles";
  private static final String FIELD = "text";

  public JobTitleNormaliser(FuzzyMatcher matcher) {
    this.matcher = matcher;
  }

  public String normalise(String input) throws IOException {
    List<String> results = matcher.match(input, INDEX, FIELD);
    return results.isEmpty() ? input : results.get(0);
  }
}
