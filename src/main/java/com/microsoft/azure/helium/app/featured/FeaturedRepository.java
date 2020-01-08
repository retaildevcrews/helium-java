package com.microsoft.azure.helium.app.featured;

import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeaturedRepository extends DocumentDbRepository<Featured, String> {

}