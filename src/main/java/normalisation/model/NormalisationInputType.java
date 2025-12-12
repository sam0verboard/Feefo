package normalisation.model;

/**
 * Normalisation input type enum. For use in NormalisationService to choose which Normaliser
 * implementation to use
 */
public enum NormalisationInputType {
  JOB_TITLE,
  // extend with other types...
  FUTURE_UNSUPPORTED_TYPE;
}
