package normalisation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Generic document for elasticsearch data import and search results. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticsearchDocument {
  private String text;
}
