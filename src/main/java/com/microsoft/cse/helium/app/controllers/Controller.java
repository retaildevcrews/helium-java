package com.microsoft.cse.helium.app.controllers;

import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.dao.ActorsDao;
import com.microsoft.cse.helium.app.dao.MoviesDao;
import com.microsoft.cse.helium.app.models.Entity;
import com.microsoft.cse.helium.app.utils.ParameterValidator;
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
  @SuppressWarnings("rawtypes")
  protected Object getAll(
      Optional<String> query, Optional<String> pageNumber, Optional<String> pageSize, Enum entity) {
    String q = null;

    if (query.isPresent()) {
      if (validator.isValidSearchQuery(query.get())) {
        q = query.get().trim().toLowerCase().replace("'", "''");
      } else {
        logger.error("Invalid q (search) parameter");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(
            "Invalid q (search) parameter", headers, HttpStatus.BAD_REQUEST);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent()) {
      if (!validator.isValidPageNumber(pageNumber.get())) {
        logger.error("Invalid PageNumber parameter");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("Invalid PageSize parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    pageNo = pageNo > 1 ? pageNo - 1 : 0;
    if (entity.equals(Entity.Actor)) {
      return actorsDao.getActors(q, pageNo * pageSz, pageSz);
    } else {
      return movieDao.getMovies(q, pageNo * pageSz, pageSz);
    }
  }
}
