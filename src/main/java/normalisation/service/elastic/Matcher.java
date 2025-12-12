package normalisation.service.elastic;

import java.io.IOException;
import java.util.List;

/**
 * Interface for different elasticsearch matching methods. Future implementations gould include
 * semantic matching
 */
public interface Matcher {
  List<String> match(String input, String index, String field) throws IOException;
}
