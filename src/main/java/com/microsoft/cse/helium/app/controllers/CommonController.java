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
import org.springframework.util.StringUtils;

@Component
public class CommonController {

  @Autowired ActorsDao actorsDao;
  @Autowired MoviesDao movieDao;

  @Autowired ParameterValidator validator;

  private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

  /** commonControllerUtilAll. */
  public Object getAll(
      Optional<String> query,
      Optional<String> pageNumber,
      Optional<String> pageSize,
      Enum entity) {
    String q = null;

    if (query.isPresent()
        && !StringUtils.isEmpty(query.get())
        && query.get() != null
        && !query.get().isEmpty()) {
      if (validator.isValidSearchQuery(query.get())) {
        q = query.get().trim().toLowerCase().replace("'", "''");
      } else {
        logger.error("Invalid q(search) parameter");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("Invalid q(search) parameter", headers, HttpStatus.BAD_REQUEST);
      }
    }

    Integer pageNo = 0;
    if (pageNumber.isPresent() && !StringUtils.isEmpty(pageNumber.get())) {
      if (!validator.isValidPageNumber(pageNumber.get())) {
        logger.error("Invalid pageNumber parameter");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(
            "Invalid pageNumber parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageNo = Integer.parseInt(pageNumber.get());
      }
    }

    Integer pageSz = Constants.DEFAULT_PAGE_SIZE;
    if (pageSize.isPresent() && !StringUtils.isEmpty(pageSize.get())) {
      if (!validator.isValidPageSize(pageSize.get())) {
        logger.error("Invalid pageSize parameter");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("Invalid pageSize parameter", headers, HttpStatus.BAD_REQUEST);
      } else {
        pageSz = Integer.parseInt(pageSize.get());
      }
    }

    try {
      pageNo--;
      if (pageNo < 0) {
        pageNo = 0;
      }

      if (entity.equals(Entity.Actor)) {
        return actorsDao.getActors(q, pageNo * pageSz, pageSz);
      } else if (entity.equals(Entity.Movie)) {
        return movieDao.getMovies(q, pageNo * pageSz, pageSz);
      } else {
        return new ResponseEntity<>(
            "ControllerException : No entity specified", HttpStatus.BAD_REQUEST);
      }
    } catch (Exception ex) {
      return new ResponseEntity<>("ControllerException", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}

