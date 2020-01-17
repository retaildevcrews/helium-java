package com.microsoft.azure.helium.app.genre;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

/**
 * GenresRepository
 */
@Repository
public interface GenresRepository extends CosmosRepository<Genre, String> {
}