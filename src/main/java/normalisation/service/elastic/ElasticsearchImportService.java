package normalisation.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import normalisation.config.DataImportProperties;
import normalisation.model.DatasetConfig;
import normalisation.model.ElasticsearchDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/** Service for importing data into Elasticsearch on startup based on configuration */
@Slf4j
@Service
public class ElasticsearchImportService {

  private final DataImportProperties properties;
  private final ElasticsearchClient elasticsearchClient;

  public ElasticsearchImportService(DataImportProperties properties, ElasticsearchClient client) {
    this.properties = properties;
    this.elasticsearchClient = client;
  }

  /**
   * Imports a JSON dataset into Elasticsearch if the index does not already exist. The method:
   * checks whether the target index exists reads items from the dataset JSON file batches items to
   * avoid oversized bulk requests indexes each item into Elasticsearch using a random document ID
   *
   * @param config dataset specific configuration including index name and JSON settings
   * @throws IOException if reading the dataset file fails
   */
  public void importData(DatasetConfig config) throws IOException {
    boolean indexExists =
        elasticsearchClient.indices().exists(e -> e.index(config.getIndexName())).value();
    List<String> items = readDataFromJson(config.getFilename(), config.getJsonRoot());

    if (!indexExists && items.size() > 0) {
      log.info("Importing dataset: {}", config.getIndexName());
      for (int i = 0; i < items.size(); i += properties.getBatchSize()) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        items
            .subList(i, Math.min(i + properties.getBatchSize(), items.size()))
            .forEach(
                title ->
                    br.operations(
                        op ->
                            op.index(
                                idx ->
                                    idx.index(config.getIndexName())
                                        .id(UUID.randomUUID().toString())
                                        .document(new ElasticsearchDocument(title)))));
        elasticsearchClient.bulk(br.build());
      }
      log.info("Finished importing: {}", config.getIndexName());
    }
  }

  private List<String> readDataFromJson(String filename, String jsonRoot) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource(filename);

    if (!resource.exists()) {
      throw new IOException(String.format("File not found on classpath: %s", filename));
    }
    InputStream is = resource.getInputStream();

    JsonNode root = mapper.readTree(is);
    JsonNode jsonNode = root.get(jsonRoot);

    List<String> data = new ArrayList<>();
    if (jsonNode != null && jsonNode.isArray()) {
      for (JsonNode textNode : jsonNode) {
        data.add(textNode.asText());
      }
    }
    return data;
  }
}
