package normalisation.controller;

import jakarta.validation.Valid;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import normalisation.model.NormalisationInput;
import normalisation.model.NormalisationOutput;
import normalisation.service.NormalisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Rest controller for normalising job titles endpoint. */
@Slf4j
@RestController
@RequestMapping("/rest/normalise")
public class NormalisationController {
  @Autowired private NormalisationService normalisationService;

  @PostMapping(
      path = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NormalisationOutput> normalise(
      @Valid @RequestBody NormalisationInput input) {
    try {
      NormalisationOutput result = normalisationService.normalise(input);
      return ResponseEntity.ok(result);
    } catch (IOException e) {
      log.error("Failed to normalise job title '{}'", input, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
