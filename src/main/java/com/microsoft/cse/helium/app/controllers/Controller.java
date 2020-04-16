package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.dao.ActorsDao;
import com.microsoft.cse.helium.app.dao.IDao;
import com.microsoft.cse.helium.app.dao.MoviesDao;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Controller {

  @Autowired ActorsDao actorsDao;
  @Autowired MoviesDao movieDao;

  @Autowired ParameterValidator validator;

  private static final Logger logger = LoggerFactory.getLogger(Controller.class);

  /** commonControllerUtilAll. */
  @SuppressWarnings("CPD-START")
  protected Object getAll(
      Optional<String> query,
      Optional<String> pageNumber,
      Optional<String> pageSize,
      IDao dataObject) {

    logger.info("controller::getAll (query=%s, pageNumber=%s, pageSize=%s)",
        query, pageNumber, pageSize);

    Map<String,Object> queryParams = new HashMap<String, Object>();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);

    if (query.isPresent()) {
      if (validator.isValidSearchQuery(query.get())) {
        queryParams.put("q", query.get().trim().toLowerCase().replace("'", "''"));
      } else {
        logger.error("Invalid q (search) parameter");

        return new ResponseEntity<>(
            "Invalid q (search) parameter", headers, HttpStatus.BAD_REQUEST);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent()) {
      if (!validator.isValidPageNumber(pageNumber.get())) {
        logger.error("Invalid PageNumber parameter");

        return new ResponseEntity<>(
            "Invalid PageNumber parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent()) {
      if (!validator.isValidPageSize(pageSize.get())) {
        logger.error("Invalid PageSize parameter");

        return new
            ResponseEntity<>("Invalid PageSize parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    pageNo = pageNo > 1 ? pageNo - 1 : 0;

    return dataObject.getAll(queryParams, pageNo * pageSz, pageSz);
  }



  /**
   * getAll.
   * @param query A variable of type String.
   * @param genre A variable of type String.
   * @param year A variable of type String.
   * @param pageNumber A variable of type String.
   * @param pageSize A variable of type String.
   */

  protected Object getAll(
      Optional<String> query,
      Optional<String> genre,
      Optional<String> year,
      Optional<String> rating,
      Optional<String> actorId,
      Optional<String> pageNumber,
      Optional<String> pageSize,
      IDao dataObject) {

    Map<String,Object> queryParams = new HashMap<String, Object>();
    String q = null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);
    
    logger.info(String.format("controller::getAll (query=%s, genre=%s, year=%s, rating=%s, "
        + "actorId=%s, pageNumber=%s, pageSize=%s)", 
        query, genre, year, rating, actorId, pageNumber, pageSize));

    if (query.isPresent()) {
      if (validator.isValidSearchQuery(query.get())) {
        q = query.get().trim().toLowerCase().replace("'", "''");
        queryParams.put("q",q);
      } else {
        logger.error("Invalid q (search) parameter");

        return new ResponseEntity<>(
            "Invalid q (search) parameter", headers, HttpStatus.BAD_REQUEST);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent()) {
      if (!validator.isValidPageNumber(pageNumber.get())) {
        logger.error("Invalid PageNumber parameter");

        return new ResponseEntity<>(
            "Invalid PageNumber parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent()) {
      if (!validator.isValidPageSize(pageSize.get())) {
        logger.error("Invalid PageSize parameter");

        return new
            ResponseEntity<>("Invalid PageSize parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    String movieGenre = "";
    if (genre.isPresent()) {
      if (!validator.isValidGenre(genre.get())) {
        logger.error("Invalid Genre parameter");

        return new
            ResponseEntity<>("Invalid Genre parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        movieGenre = genre.get();
        queryParams.put("genre", movieGenre);
      }
    }

    Integer movieYear = 0;
    if (year.isPresent()) {
      if (!validator.isValidYear(year.get())) {
        logger.error("Invalid Year parameter");

        return new
            ResponseEntity<>("Invalid Year parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        movieYear = Integer.parseInt(year.get());
        queryParams.put("year", movieYear);
      }
    }

    Double movieRating = 0.0;
    if (rating.isPresent()) {
      if (!validator.isValidRating(rating.get())) {
        logger.error("Invalid Rating parameter");

        return new
            ResponseEntity<>("Invalid Rating parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        movieRating = Double.parseDouble(rating.get());
        queryParams.put("ratingSelect", movieRating);
      }
    }

    String movieActorId = "";
    if (actorId.isPresent()) {
      if (!validator.isValidActorId(actorId.get())) {
        logger.error("Invalid Actor ID parameter");

        return new ResponseEntity<>(
            "Invalid Actor ID parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        movieActorId = actorId.get();
        queryParams.put("actorSelect", movieActorId);
      }
    }

    pageNo = pageNo > 1 ? pageNo - 1 : 0;

    return dataObject.getAll(queryParams, pageNo * pageSz, pageSz);
  }
}
