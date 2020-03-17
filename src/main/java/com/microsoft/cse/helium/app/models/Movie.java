package com.microsoft.cse.helium.app.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import com.microsoft.cse.helium.app.Constants;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

/**
 * Movie.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = Constants.DEFAULT_MOVIE_COLLECTION_NAME)

@JsonPropertyOrder({"id", "movieId", "partitionKey", "type", "title", "textSearch", "year",
    "runtime", "rating", "votes", "totalScore", "genres", "roles"})
public class Movie extends MovieBase {

  @JsonIgnore
  @Id
  private String id;

  @PartitionKey
  private String partitionKey;

  private double rating;
  private long votes;
  private long totalScore;
  private String textSearch;
  private List<Role> roles;


  public Movie(){

  }
}