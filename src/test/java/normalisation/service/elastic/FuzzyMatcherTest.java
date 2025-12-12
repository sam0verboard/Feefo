package normalisation.service.elastic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.util.ObjectBuilder;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import normalisation.model.ElasticsearchDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FuzzyMatcherTest {

  public FuzzyMatcher fuzzyMatcher;
  private final String FIELD = "text";
  private final String INDEX = "job_titles";

  @Mock private ElasticsearchClient elasticsearchClient;

  @BeforeEach
  void setup() {
    fuzzyMatcher = new FuzzyMatcher(elasticsearchClient);
  }

  @Test
  void fuzzyMatch_thenEmptyOutput_whenBlankInput() throws IOException {
    List<String> match = fuzzyMatcher.match("", INDEX, FIELD);
    assertEquals(0, match.size());
  }

  @Test
  void fuzzyMatch_thenEmptyOutput_whenNullInput() throws IOException {
    List<String> match = fuzzyMatcher.match(null, INDEX, FIELD);
    assertEquals(0, match.size());
  }

  @Test
  void fuzzyMatch_throwsException_whenClientThrowsException() throws IOException {
    when(elasticsearchClient.search(
            (Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>)
                ArgumentMatchers.<Function<SearchRequest.Builder, ?>>any(),
            eq(ElasticsearchDocument.class)))
        .thenThrow(IOException.class);

    assertThrows(IOException.class, () -> fuzzyMatcher.match("input", INDEX, FIELD));
  }

  @Test
  void fuzzyMatch_givesOutput_whenValidInputGiven() throws IOException {
    String input = "Software engineer";
    Hit<ElasticsearchDocument> hit1 = mock(Hit.class);

    when(hit1.source()).thenReturn(new ElasticsearchDocument(input));

    SearchResponse<ElasticsearchDocument> response = mock(SearchResponse.class);
    when(response.hits()).thenReturn(mock(HitsMetadata.class));
    when(response.hits().hits()).thenReturn(List.of(hit1));

    when(elasticsearchClient.search(
            (Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>>)
                ArgumentMatchers.<Function<SearchRequest.Builder, ?>>any(),
            eq(ElasticsearchDocument.class)))
        .thenReturn(response);

    List<String> match = fuzzyMatcher.match(input, INDEX, FIELD);
    assertEquals(1, match.size());
    assertEquals(input, match.get(0));
  }
}
