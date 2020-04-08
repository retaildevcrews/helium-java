package com.microsoft.cse.helium.app.dao;

import java.util.Map;
import reactor.core.publisher.Flux;

public interface IDao {

  Flux<?> getAll(Map<String, Object> queryParams, Integer pageNumber, Integer pageSize);
}
