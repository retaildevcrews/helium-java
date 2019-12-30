package com.microsoft.azure.helium.app.genre;

import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;

import org.springframework.stereotype.Repository;

/**
 * GenresRepository
 */
@Repository
public interface GenresRepository extends DocumentDbRepository<Genre, String> {
}