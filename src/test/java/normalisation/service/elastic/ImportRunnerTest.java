package normalisation.service.elastic;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import normalisation.config.DataImportProperties;
import normalisation.model.DatasetConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

@ExtendWith(MockitoExtension.class)
class ImportRunnerTest {

  @Mock private DataImportProperties properties;

  @Mock private ElasticsearchImportService importService;

  @Mock private ApplicationArguments args;

  private ImportRunner importRunner;

  @BeforeEach
  void setup() {
    importRunner = new ImportRunner(properties, importService);
  }

  @Test
  void run_doesNotImportDataSet_whenFlagFalse() throws Exception {
    DatasetConfig dataset1 = mock(DatasetConfig.class);
    DatasetConfig dataset2 = mock(DatasetConfig.class);

    when(dataset1.isImportOnStartup()).thenReturn(true);
    when(dataset2.isImportOnStartup()).thenReturn(false);

    Map<String, DatasetConfig> datasets = new HashMap<>();
    datasets.put("dataset1", dataset1);
    datasets.put("dataset2", dataset2);

    when(properties.getDatasets()).thenReturn(datasets);

    importRunner.run(args);

    verify(importService, times(1)).importData(dataset1);
    verify(importService, never()).importData(dataset2);
  }

  @Test
  void run_doesNotCallImportService_whenDataSetEmpty() throws Exception {
    when(properties.getDatasets()).thenReturn(new HashMap<>());

    importRunner.run(args);

    verify(importService, never()).importData(any());
  }

  @Test
  void run_importsAllDataSets_whenAllHaveFlagTrue() throws Exception {
    DatasetConfig d1 = mock(DatasetConfig.class);
    DatasetConfig d2 = mock(DatasetConfig.class);
    when(d1.isImportOnStartup()).thenReturn(true);
    when(d2.isImportOnStartup()).thenReturn(true);

    Map<String, DatasetConfig> datasets = Map.of("d1", d1, "d2", d2);
    when(properties.getDatasets()).thenReturn(datasets);

    importRunner.run(args);

    verify(importService).importData(d1);
    verify(importService).importData(d2);
    verify(importService, times(2)).importData(any());
  }
}
