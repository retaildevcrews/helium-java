package com.cse.helium.app.utils;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InvalidParameterResponses {

  private static final String VALIDATION_ERROR_TEMPLATE = "    {\n"
      + "      \"code\": \"InvalidValue\",\n"
      + "      \"target\": \"%s\",\n"
      + "      \"message\": \"%s\"\n"
      + "    }";

  private static final String Q_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE, 
      "q",
      "The parameter 'q' should be between 2 and 20 characters.");

  private static final String PAGESIZE_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "pageSize",
      "The parameter 'pageSize' should be between 1 and 1000.");

  private static final String PAGENUM_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "pageNumber",
      "The parameter 'pageNumber' should be between 1 and 10000.");

  private static final String GENRE_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "genre",
      "The parameter 'genre' should be between 3 and 20 characters.");

  private static final String YEAR_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "year",
      "The parameter 'year' should be between 1874 and 2025.");

  private static final String RATING_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "rating",
      "The parameter 'rating' should be between 0.0 and 10.0.");

  private static final String ACTORID_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "actorId",
      "The parameter 'actorId' should start with 'nm' and be between 7 and 11 characters in total."
      );
  
  private static final String MOVIEID_ERROR = String.format(
      VALIDATION_ERROR_TEMPLATE,
      "movieId",
      "The parameter 'movieId' should start with 'tt' and be between 7 and 11 characters in total."
      );

  public enum SearchParameter {
    Q,
    PAGE_SIZE,
    PAGE_NUMBER,
    YEAR,
    GENRE,
    RATING,
    ACTOR_ID
  }

  private static final String DOC_URL = "https://github.com/retaildevcrews/helium/blob/main/docs/ParameterValidation.md";

  InvalidParameterResponses() {
  }

  private String parameterToValidationError(SearchParameter parameter) {
    switch (parameter) {
      case Q:
        return Q_ERROR;
      case PAGE_SIZE:
        return PAGESIZE_ERROR;
      case PAGE_NUMBER:
        return PAGENUM_ERROR;
      case YEAR:
        return YEAR_ERROR;
      case GENRE:
        return GENRE_ERROR;
      case RATING:
        return RATING_ERROR;
      case ACTOR_ID:
        return ACTORID_ERROR;
      default:
        return "";
    }
  }

  private String parametersToValidationErrors(List<SearchParameter> parameters) {
    List<String> validationErrors = parameters.stream().map(this::parameterToValidationError)
        .collect(Collectors.toList());
    return String.join(",\n", validationErrors);
  }

  private String response(String type, String instance, String validationErrorsBody) {
    return "{\n"
      + "  \"type\": \"" + type + "\",\n"
      + "  \"title\": \"Parameter validation error\",\n"
      + "  \"detail\": \"One or more invalid parameters were specified.\",\n"
      + "  \"status\": 400,\n"
      + "  \"instance\": \"" + instance + "\",\n"
      + "  \"validationErrors\": [\n"
      +    validationErrorsBody + "\n"
      + "  ]\n"
      + "}";
  }

  public String invalidActorSearchResponse(String instance, List<SearchParameter> parameters) {
    return response(DOC_URL + "#actors", instance, parametersToValidationErrors(parameters));
  }

  public String invalidMovieSearchResponse(String instance, List<SearchParameter> parameters) {
    return response(DOC_URL + "#movies", instance, parametersToValidationErrors(parameters));
  }

  public String invalidActorDirectReadResponse(String instance) {
    return response(DOC_URL + "#direct-read-1", instance, ACTORID_ERROR);
  }

  public String invalidMovieDirectReadResponse(String instance) {
    return response(DOC_URL + "#direct-read", instance, MOVIEID_ERROR);
  }
  
}
