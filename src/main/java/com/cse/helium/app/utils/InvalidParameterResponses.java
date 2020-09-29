package com.cse.helium.app.utils;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class InvalidParameterResponses {

  private static final String VALIDATION_ERROR_TEMPLATE = "{    \n"
      + "      \"code\": \"InvalidValue\",\n"
      + "      \"target\": \"{0}\",\n"
      + "      \"message\": \"{1}\",\n"
      + "    }";

  private static final String Q_ERROR = MessageFormat.format(
    VALIDATION_ERROR_TEMPLATE, 
    "Q",
    "The parameter 'q' should be between 2 and 20 characters.");

  private static final String PAGESIZE_ERROR = MessageFormat.format(
    VALIDATION_ERROR_TEMPLATE,
    "PageSize",
    "The parameter 'PageSize' should be between 1 and 1000.");

  private static final String PAGENUM_ERROR = MessageFormat.format(
    VALIDATION_ERROR_TEMPLATE,
    "PageNumber",
    "The parameter 'PageNumber' should be between 1 and 10000.");

  private static final String GENRE_ERROR = MessageFormat.format(
    VALIDATION_ERROR_TEMPLATE,
    "Genre",
    "The parameter 'Genre' should be between 3 and 20 characters.");

  private static final String YEAR_ERROR = MessageFormat.format(
    VALIDATION_ERROR_TEMPLATE,
    "Year",
    "The parameter 'Year' should be between 1874 and 2025.");

  private static final String ACTORID_ERROR = MessageFormat.format(
    VALIDATION_ERROR_TEMPLATE,
    "ActorId",
    "The parameter 'ActorId' should start with 'nm' and be between 7 and 11 characters in total");
  
  private static final String MOVIEID_ERROR = MessageFormat.format(
    VALIDATION_ERROR_TEMPLATE,
    "MovieId",
    "The parameter 'MovieId' should start with 'tt' and be between 7 and 11 characters in total");

  public enum SearchParameter {
    Q,
    PAGE_SIZE,
    PAGE_NUMBER,
    YEAR,
    GENRE,
    ACTOR_ID
  }

  InvalidParameterResponses() {}

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
      case ACTOR_ID:
        return ACTORID_ERROR;
      default:
        return "";
    }
  }

  private String parametersToValidationErrors(List<SearchParameter> paramters) {
    List<String> validationErrors = paramters.stream().map(this::parameterToValidationError).collect(Collectors.toList());
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
    return response("https://github.com/retaildevcrews/helium/blob/main/docs/ParameterValidation.md#actors", instance, parametersToValidationErrors(parameters));
  }

  public String invalidMovieSearchResponse(String instance, List<SearchParameter> parameters) {
    return response("https://github.com/retaildevcrews/helium/blob/main/docs/ParameterValidation.md#movies", instance, parametersToValidationErrors(parameters));
  }

  public String invalidActorDirectReadResponse(String instance) {
    return response("https://github.com/retaildevcrews/helium/blob/main/docs/ParameterValidation.md#direct-read-1", instance, ACTORID_ERROR);
  }

  public String invalidMovieDirectReadResponse(String instance) {
    return response("https://github.com/retaildevcrews/helium/blob/main/docs/ParameterValidation.md#direct-read", instance, MOVIEID_ERROR);
  }
  
}
