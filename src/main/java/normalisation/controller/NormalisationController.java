package normalisation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import normalisation.model.NormalisationInput;
import normalisation.model.NormalisationOutput;
import normalisation.service.NormalisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/normalise")
public class NormalisationController {

    @Autowired
    private NormalisationService normalisationService;

    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NormalisationOutput> normaliseJobTitle(
            @Valid @RequestBody NormalisationInput input) {
        return new ResponseEntity<>(normalisationService.normalise(input), HttpStatus.OK);
    }


    // Move to exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .filter(objectError -> objectError instanceof FieldError)
                .map(objectError -> (FieldError) objectError)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

}
