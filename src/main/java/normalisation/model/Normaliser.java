package normalisation.model;

import java.io.IOException;

/**
 * Extendable interface for normalisers. Allows different input types to be normalised with
 * different methods.
 */
public interface Normaliser {

  String normalise(String input) throws IOException;
}
