package com.microsoft.cse.helium.app.utils;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/** ParameterValidator. */
@Component
public class ParameterValidator {

  /* Valid input: starts with 'nm' (case sensitive)
      followed by 5-9 digits
      parses to int > 0
  */
  private static final String validActorRegex = "[nm]{2}[0-9]{5,9}";

  /** isValidActorId. */
  public Boolean isValidActorId(String actorId) {
    Pattern p = Pattern.compile(validActorRegex);
    return p.matcher(actorId).matches();
  }

  /** isValidSearchQuery. */
  public Boolean isValidSearchQuery(String query) {
    Boolean isValid = true;
    if (query.length() < 2 || query.length() > 20) {
      isValid = false;
    }
    return isValid;
  }

  /** isValidPageNumber. */
  public Boolean isValidPageNumber(String pageNumber) {
    Boolean isValid = true;
    try {
      Integer pageNo = Integer.parseInt(pageNumber);
      if (pageNo < 1 || pageNo > 10000) {
        isValid = false;
      }
    } catch (Exception ex) {
      isValid = false;
    }
    return isValid;
  }

  /**
   * isValidPageSize.
   */
  public Boolean isValidPageSize(String pageSize) {
    Boolean isValid = true;
    try {
      Integer pageSz = Integer.parseInt(pageSize);
      if (pageSz < 1 || pageSz > 1000) {
        isValid = false;
      }
    } catch (Exception ex) {
      isValid = false;
    }
    return isValid;
  }
}
