package normalisation.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import normalisation.model.ElasticsearchDocument;
import org.springframework.stereotype.Component;

/**
 * Performs fuzzy matching against an Elasticsearch index. This component returns the top matching
 * documents for the given field.
 */
@Slf4j
@Component
public class FuzzyMatcher implements Matcher {

  private static final String FUZZINESS = "AUTO";
  private static final Integer RESPONSE_SIZE = 1;

  private final ElasticsearchClient elasticsearchClient;

  public FuzzyMatcher(ElasticsearchClient elasticsearchClient) {
    this.elasticsearchClient = elasticsearchClient;
  }

  @Override
  public List<String> match(String input, String index, String field) throws IOException {
    if (input == null || input.isBlank()) {
      return Collections.emptyList();
    }
    SearchResponse<ElasticsearchDocument> response =
        elasticsearchClient.search(
            s ->
                s.index(index)
                    .size(RESPONSE_SIZE)
                    .query(q -> q.match(m -> m.field(field).query(input).fuzziness(FUZZINESS))),
            ElasticsearchDocument.class);

    // Null checks on results
    List<Hit<ElasticsearchDocument>> hits =
        response.hits() == null || response.hits().hits() == null
            ? List.of()
            : response.hits().hits();

    return hits.stream()
        .map(Hit::source)
        .filter(Objects::nonNull)
        .map(ElasticsearchDocument::getText)
        .filter(Objects::nonNull)
        .toList();
  }
}
