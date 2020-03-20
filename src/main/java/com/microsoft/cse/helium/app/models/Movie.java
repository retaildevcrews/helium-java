package com.microsoft.cse.helium.app.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import com.microsoft.cse.helium.app.Constants;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@EqualsAndHashCode(callSuper = true)
@Document(collection = Constants.DEFAULT_MOVIE_COLLECTION_NAME)

@JsonPropertyOrder({"id", "movieId", "partitionKey", "type", "title", "textSearch", "year",
    "runtime", "rating", "votes", "totalScore", "genres", "roles"})
public class Movie extends MovieBase {

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  @JsonIgnore
  @Id
  private String id;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  @PartitionKey
  private String partitionKey;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private double rating;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private long votes;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private long totalScore;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private String textSearch;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private List<Role> roles;


  public Movie(){

  }
}