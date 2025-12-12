package normalisation.model;

import lombok.Data;

/** POJO for dataset import configuration. */
@Data
public class DatasetConfig {
  private boolean importOnStartup;
  private String filename;
  private String jsonRoot;
  private String indexName;
}
