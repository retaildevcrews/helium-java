package com.microsoft.azure.helium.app.featured;

import com.microsoft.azure.helium.app.Constants;
import com.microsoft.azure.helium.app.movie.Movie;
import com.microsoft.azure.helium.app.movie.MovieBase;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;


/**
 * Featured
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = Constants.DEFAULT_FEATURED_COLLECTION_NAME)

public class Featured {
    @JsonIgnore
    @Id
    private String id;

    @PartitionKey
    private String partitionKey;

    private String movieId;
    private Integer weight;


}