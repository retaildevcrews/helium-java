package com.microsoft.cse.helium.app.dao;

import reactor.core.publisher.Flux;

public interface IDao {
  Flux<?> getAll(String query, Integer pageNumber, Integer pageSize);
}