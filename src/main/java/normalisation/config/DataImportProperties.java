package normalisation.config;

import java.util.Map;
import lombok.Data;
import normalisation.model.DatasetConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Imports elastic configuration properties from application.yaml. */
@Data
@Configuration
@ConfigurationProperties(prefix = "data")
public class DataImportProperties {
  private Integer batchSize;
  private Integer port;
  private String hostname;
  private Map<String, DatasetConfig> datasets;
}
