package com.microsoft.cse.helium.app.utils;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** ParameterValidator. */
@Component
public class ParameterValidator {

  /* Valid input: starts with 'nm' (case sensitive)
      followed by 5-9 digits
      parses to int > 0
  */
  private static final String validActorRegex = "[nm]{2}[0-9]{5,9}";

  private static final String validMovieRegex = "[tt]{2}[0-9]{5,9}";

  /** isValidActorId. */
  public Boolean isValidActorId(String actorId) {
    Pattern p = Pattern.compile(validActorRegex);
    return p.matcher(actorId).matches();
  }

  /** isValidMovieId. */
  public Boolean isValidMovieId(String movieId) {
    Pattern p = Pattern.compile(validMovieRegex);
    return p.matcher(movieId).matches();
  }

  /** isValidSearchQuery. */
  public Boolean isValidSearchQuery(String query) {
    if (!StringUtils.isEmpty(query) && (query.length() < 2 || query.length() > 20)) {
      return false;
    }
    return true;
  }

  /** isValidPageNumber. */
  public Boolean isValidPageNumber(String pageNumber) {
    if (!StringUtils.isEmpty(pageNumber)) {
      try {
        Integer pageNo = Integer.parseInt(pageNumber);
        if (pageNo < 1 || pageNo > 10000) {
          return false;
        }
      } catch (Exception ex) {
        return false;
      }
    }
    return true;
  }

  /** isValidPageSize. */
  public Boolean isValidPageSize(String pageSize) {
    if (!StringUtils.isEmpty(pageSize)) {
      try {
        Integer pageSz = Integer.parseInt(pageSize);
        if (pageSz < 1 || pageSz > 1000) {
          return false;
        }
      } catch (Exception ex) {
        return false;
      }
    }
    return true;
  }
}
