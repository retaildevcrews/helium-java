package com.microsoft.cse.helium.app.utils;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** ParameterValidator. */
@Component
public class ParameterValidator {

  /**
   * This method will return true if input is int and false otherwise.
   * @param s input string to test as an int
   * @return boolean
   */
  public static boolean isInteger(String s) {
    boolean isValidInteger = false;
    try {
      Integer.parseInt(s);
      isValidInteger = true;
    } catch (NumberFormatException ex) {
      isValidInteger = false;
    }

    return isValidInteger;
  }

  /** isValidActorId. */
  public Boolean isValidActorId(String actorId) {
    return isValidId(actorId, "nm");
  }

  /** isValidMovieId. */
  public Boolean isValidMovieId(String movieId) {
    return isValidId(movieId, "tt");
  }

  /**
   * Used to check for valid ID input.
   * @param id id which is checked not nul 7 <= id <= 11
   * @param prefixToMatch first two characters of id must match
   * @return boolean
   */
  public Boolean isValidId(String id, String prefixToMatch) {
    boolean retValue = false;
    if (id == null 
        || id.isEmpty() 
        || id.length() < 7 
        || id.length() > 11 
        || !id.substring(0, 2).equals(prefixToMatch) 
        || !isInteger(id.substring(2))
        || Integer.parseInt(id.substring(2)) <= 0) {
      retValue = false;
    } else {
      retValue = true;
    }
    return retValue;
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

  /** isValidGenre. */
  public Boolean isValidGenre(String genre) {
    if (!StringUtils.isEmpty(genre)) {
      try {
        if (genre.length() < 3 || genre.length() > 20) {
          return false;
        }
      } catch (Exception ex) {
        return false;
      }
    }
    return true;
  }

  /** isValidYear. */
  public Boolean isValidYear(String year) {
    if (!StringUtils.isEmpty(year)) {
      try {
        Integer movieYear = Integer.parseInt(year);
        if (movieYear < 1874 || movieYear > OffsetDateTime.now().getYear() + 5) {
          return false;
        }
      } catch (Exception ex) {
        return false;
      }
    }
    return true;
  }

  /** isValidRating. */
  public Boolean isValidRating(String rating) {
    if (!StringUtils.isEmpty(rating)) {
      try {
        Integer movieRating = Integer.parseInt(rating);
        if (movieRating < 0 || movieRating > 10) {
          return false;
        }
      } catch (Exception ex) {
        return false;
      }
    }
    return true;
  }

}
