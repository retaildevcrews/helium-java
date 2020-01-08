package com.microsoft.azure.helium.app.featured;

import com.microsoft.azure.spring.data.cosmosdb.core.query.DocumentDbPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * ActorsService
 */
@Service
public class FeaturedService {

    @Autowired
    private FeaturedRepository repository;

    public List<Featured> getAllFeaturedMovies() {
            return (List<Featured>) repository.findAll();
    }

}