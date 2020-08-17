package com.cse.helium.app.controllers;

import com.cse.helium.app.Constants;
import com.cse.helium.app.dao.IDao;
import com.cse.helium.app.utils.ParameterValidator;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Controller {

  @Autowired ParameterValidator validator;
  private static final Logger logger =   LogManager.getLogger(Controller.class);

  /** commonControllerUtilAll. */

  public Object getAll(
      Optional<String> query,
      Optional<String> pageNumber,
      Optional<String> pageSize,
      IDao dataObject) {

    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat
          .format("controller::getAll (query={0}, pageNumber={1}, pageSize={2})",
          query, pageNumber, pageSize));
    }

    Map<String,Object> queryParams = new HashMap<>();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    if (query.isPresent()) {
      if (Boolean.TRUE.equals(validator.isValidSearchQuery(query.get()))) {
        queryParams.put("q", query.get().trim().toLowerCase().replace("'", "''"));
      } else {
        logger.error("INVALID_QUERY_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_QUERY_MESSAGE, HttpStatus.BAD_REQUEST);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageNumber(pageNumber.get()))) {
        logger.error("INVALID_PAGENUM_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_PAGENUM_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageSize(pageSize.get()))) {
        logger.error("INVALID_PAGESIZE_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_PAGESIZE_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    pageNo = pageNo > 1 ? pageNo - 1 : 0;

    return dataObject.getAll(queryParams, pageNo * pageSz, pageSz);
  }

  /**
   * getAll.
   *
   * @param query A variable of type String.
   * @param genre A variable of type String.
   * @param year A variable of type String.
   * @param pageNumber A variable of type String.
   * @param pageSize A variable of type String.
   */
  // suppressing cyclomatic complexity (S3776) and param count (S107)
  @SuppressWarnings({"CPD-START", "squid:S3776", "squid:S107", "squid:S00107"})
  public Object getAll(
      Optional<String> query,
      Optional<String> genre,
      Optional<String> year,
      Optional<String> rating,
      Optional<String> actorId,
      Optional<String> pageNumber,
      Optional<String> pageSize,
      IDao dataObject) {

    Map<String,Object> queryParams = new HashMap<>();
    String q = null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("controller::getAll (query={0}, genre={1}, year={2}, "
          + "rating={3}, actorId={4}, pageNumber={5}, pageSize={6})",
          query, genre, year, rating, actorId, pageNumber, pageSize));
    }

    if (query.isPresent()) {
      if (Boolean.TRUE.equals(validator.isValidSearchQuery(query.get()))) {
        q = query.get().trim().toLowerCase().replace("'", "''");
        queryParams.put("q",q);
      } else {
        logger.error("INVALID_QUERY_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_QUERY_MESSAGE,
            HttpStatus.BAD_REQUEST);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageNumber(pageNumber.get()))) {
        logger.error("INVALID_PAGENUM_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_PAGENUM_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageSize(pageSize.get()))) {
        logger.error("INVALID_PAGESIZE_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_PAGESIZE_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    String movieGenre = "";
    if (genre.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidGenre(genre.get()))) {
        logger.error("INVALID_GENRE_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_GENRE_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        movieGenre = genre.get();
        queryParams.put("genre", movieGenre);
      }
    }

    Integer movieYear = 0;
    if (year.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidYear(year.get()))) {
        logger.error("INVALID_YEAR_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_YEAR_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        movieYear = Integer.parseInt(year.get());
        queryParams.put("year", movieYear);
      }
    }

    Double movieRating;
    if (rating.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidRating(rating.get()))) {
        logger.error("INVALID_RATING_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_RATING_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        movieRating = Double.parseDouble(rating.get());
        queryParams.put("ratingSelect", movieRating);
      }
    }

    String movieActorId = "";
    if (actorId.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidActorId(actorId.get()))) {
        logger.error("INVALID_ACTORID_MESSAGE");

        return new ResponseEntity<String>(Constants.INVALID_ACTORID_MESSAGE,
            HttpStatus.BAD_REQUEST);
      } else {
        movieActorId = actorId.get();
        queryParams.put("actorSelect", movieActorId);
      }
    }

    pageNo = pageNo > 1 ? pageNo - 1 : 0;

    return dataObject.getAll(queryParams, pageNo * pageSz, pageSz);
  }
}
