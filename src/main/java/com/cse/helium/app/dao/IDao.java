package com.cse.helium.app.dao;

import java.util.Map;
import reactor.core.publisher.Flux;

@SuppressWarnings ("squid:S1452")  // suppress removing wildard <?> type as is desired for interface
public interface IDao {

  Flux<?> getAll(Map<String, Object> queryParams, Integer pageNumber, Integer pageSize);
}
