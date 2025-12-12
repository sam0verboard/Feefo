package normalisation.service.elastic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.util.ObjectBuilder;
import java.io.IOException;
import java.util.function.Function;
import normalisation.config.DataImportProperties;
import normalisation.model.DatasetConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ElasticsearchImportServiceTest {

  @Mock private DataImportProperties properties;

  @Mock private ElasticsearchClient elasticsearchClient;

  @Mock private ElasticsearchIndicesClient indicesClient;

  private ElasticsearchImportService importService;

  private DatasetConfig config;

  @BeforeEach
  void setup() {
    when(elasticsearchClient.indices()).thenReturn(indicesClient);

    importService = new ElasticsearchImportService(properties, elasticsearchClient);

    config = new DatasetConfig();
    config.setIndexName("test-index");
    config.setFilename("test-data.json");
    config.setJsonRoot("test-data");
  }

  @Test
  void importData_skipsIfIndexExists() throws IOException {
    when(indicesClient.exists(
            (Function<ExistsRequest.Builder, ObjectBuilder<ExistsRequest>>) any()))
        .thenReturn(new BooleanResponse(true));
    importService.importData(config);
    verify(elasticsearchClient, never()).bulk((BulkRequest) any());
  }

  @Test
  void importData_readsJsonAndBulkIndexes() throws IOException {
    when(indicesClient.exists(
            (Function<ExistsRequest.Builder, ObjectBuilder<ExistsRequest>>) any()))
        .thenReturn(new BooleanResponse(false));

    when(properties.getBatchSize()).thenReturn(2);

    importService.importData(config);

    ArgumentCaptor<BulkRequest> captor = ArgumentCaptor.forClass(BulkRequest.class);
    verify(elasticsearchClient, times(2)).bulk(captor.capture());

    BulkRequest firstBatch = captor.getAllValues().get(0);
    assertEquals(2, firstBatch.operations().size());

    BulkRequest secondBatch = captor.getAllValues().get(1);
    assertEquals(1, secondBatch.operations().size());
  }

  @Test
  void readDataFromJson_throwsIOExceptionWhenFileMissing() throws IOException {
    DatasetConfig invalidConfig = new DatasetConfig();
    invalidConfig.setFilename("nonexistent.json");

    when(indicesClient.exists(
            (Function<ExistsRequest.Builder, ObjectBuilder<ExistsRequest>>) any()))
        .thenReturn(new BooleanResponse(false));

    assertThrows(IOException.class, () -> importService.importData(invalidConfig));
  }
}
