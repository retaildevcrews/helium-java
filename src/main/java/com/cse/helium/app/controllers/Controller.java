package com.cse.helium.app.controllers;

import com.cse.helium.app.Constants;
import com.cse.helium.app.dao.IDao;
import com.cse.helium.app.utils.InvalidParameterResponses;
import com.cse.helium.app.utils.InvalidParameterResponses.SearchParameter;
import com.cse.helium.app.utils.ParameterValidator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Controller {

  @Autowired ParameterValidator validator;
  @Autowired InvalidParameterResponses invalidParameterResponses;
  private static final Logger logger =   LogManager.getLogger(Controller.class);

  /** commonControllerUtilAll. */

  public Object getAll(
      Optional<String> query,
      Optional<String> pageNumber,
      Optional<String> pageSize,
      IDao dataObject,
      String contextPath) {

    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat
          .format("controller::getAll (query={0}, pageNumber={1}, pageSize={2})",
          query, pageNumber, pageSize));
    }

    List<SearchParameter> invalidParameters = new ArrayList<>();

    Map<String,Object> queryParams = new HashMap<>();

    if (query.isPresent()) {
      if (Boolean.TRUE.equals(validator.isValidSearchQuery(query.get()))) {
        queryParams.put("q", query.get().trim().toLowerCase().replace("'", "''"));
      } else {
        logger.error(MessageFormat.format("Invalid Actor Query parameter {0}", query.get()));
        invalidParameters.add(SearchParameter.Q);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageNumber(pageNumber.get()))) {
        logger.error(MessageFormat.format("Invalid Actor PageNumber parameter {0}",
            pageNumber.get()));
        invalidParameters.add(SearchParameter.PAGE_NUMBER);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageSize(pageSize.get()))) {
        logger.error(MessageFormat.format("Invalid Actor PageSize parameter {0}", pageSize.get()));
        invalidParameters.add(SearchParameter.PAGE_SIZE);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    if (invalidParameters.isEmpty()) {
      pageNo = pageNo > 1 ? pageNo - 1 : 0;
      return dataObject.getAll(queryParams, pageNo * pageSz, pageSz);
    } else {
      return ResponseEntity.badRequest()
          .contentType(MediaType.APPLICATION_PROBLEM_JSON)
          .body(invalidParameterResponses
              .invalidActorSearchResponse(contextPath, invalidParameters));
    }

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
      IDao dataObject,
      String contextPath) {

    Map<String,Object> queryParams = new HashMap<>();
    String q = null;

    if (logger.isInfoEnabled()) {
      logger.info(MessageFormat.format("controller::getAll (query={0}, genre={1}, year={2}, "
          + "rating={3}, actorId={4}, pageNumber={5}, pageSize={6})",
          query, genre, year, rating, actorId, pageNumber, pageSize));
    }

    List<SearchParameter> invalidParameters = new ArrayList<>();

    if (query.isPresent()) {
      if (Boolean.TRUE.equals(validator.isValidSearchQuery(query.get()))) {
        q = query.get().trim().toLowerCase().replace("'", "''");
        queryParams.put("q",q);
      } else {
        logger.error(MessageFormat.format("Invalid Movie Query parameter {0}", query.get()));
        invalidParameters.add(SearchParameter.Q);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageNumber(pageNumber.get()))) {
        logger.error(MessageFormat.format("Invalid Movie PageNumber parameter {0}",
            pageNumber.get()));
        invalidParameters.add(SearchParameter.PAGE_NUMBER);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidPageSize(pageSize.get()))) {
        logger.error(MessageFormat.format("Invalid Movie PageSize parameter {0}", pageSize.get()));
        invalidParameters.add(SearchParameter.PAGE_SIZE);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    String movieGenre = "";
    if (genre.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidGenre(genre.get()))) {
        logger.error(MessageFormat.format("Invalid Movie Genre parameter {0}", genre.get()));
        invalidParameters.add(SearchParameter.GENRE);
      } else {
        movieGenre = genre.get();
        queryParams.put("genre", movieGenre);
      }
    }

    Integer movieYear = 0;
    if (year.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidYear(year.get()))) {
        logger.error(MessageFormat.format("Invalid Movie Year parameter {0}", year.get()));
        invalidParameters.add(SearchParameter.YEAR);
      } else {
        movieYear = Integer.parseInt(year.get());
        queryParams.put("year", movieYear);
      }
    }

    Double movieRating;
    if (rating.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidRating(rating.get()))) {
        logger.error(MessageFormat.format("Invalid Movie Rating parameter {0}", rating.get()));
        invalidParameters.add(SearchParameter.RATING);
      } else {
        movieRating = Double.parseDouble(rating.get());
        queryParams.put("ratingSelect", movieRating);
      }
    }

    String movieActorId = "";
    if (actorId.isPresent()) {
      if (Boolean.FALSE.equals(validator.isValidActorId(actorId.get()))) {
        logger.error(MessageFormat.format("Invalid Movie ActorID parameter {0}", actorId.get()));
        invalidParameters.add(SearchParameter.ACTOR_ID);
      } else {
        movieActorId = actorId.get();
        queryParams.put("actorSelect", movieActorId);
      }
    }

    if (invalidParameters.isEmpty()) {
      pageNo = pageNo > 1 ? pageNo - 1 : 0;
      return dataObject.getAll(queryParams, pageNo * pageSz, pageSz);
    } else {
      return ResponseEntity.badRequest()
          .contentType(MediaType.APPLICATION_PROBLEM_JSON)
          .body(invalidParameterResponses
              .invalidMovieSearchResponse(contextPath, invalidParameters));
    }

  }
}
