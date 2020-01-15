package com.microsoft.azure.helium.app.featured;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeaturedRepository extends CosmosRepository<Featured, String> {
}