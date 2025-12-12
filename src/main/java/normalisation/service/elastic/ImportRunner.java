package normalisation.service.elastic;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import normalisation.config.DataImportProperties;
import normalisation.model.DatasetConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** Runs import on startup for datasets based on configuration */
@Slf4j
@Component
public class ImportRunner implements ApplicationRunner {

  private final DataImportProperties properties;
  private final ElasticsearchImportService importService;

  public ImportRunner(DataImportProperties properties, ElasticsearchImportService importService) {
    this.properties = properties;
    this.importService = importService;
  }

  /**
   * Runs on startup to import a JSON datasets into Elasticsearch based on configuration in
   * application.yaml. The method: checks whether the dataset should be imported. imports required
   * data into Elasticsearch
   *
   * @param args Application arguments
   * @throws Exception if the import fails
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {
    for (Map.Entry<String, DatasetConfig> entry : properties.getDatasets().entrySet()) {
      DatasetConfig config = entry.getValue();
      if (!config.isImportOnStartup()) continue;
      importService.importData(config);
    }
  }
}
