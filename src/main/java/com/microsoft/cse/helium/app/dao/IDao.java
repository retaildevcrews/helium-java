package com.microsoft.cse.helium.app.dao;

import reactor.core.publisher.Flux;

public interface IDao {

  Flux<?> getAll(String query, String genre, Integer year, Integer rating,
                 String actorId,
                 Integer pageNumber, Integer pageSize);

  Flux<?> getAll(String query, Integer pageNumber, Integer pageSize);
}
