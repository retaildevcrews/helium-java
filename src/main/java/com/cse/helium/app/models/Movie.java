package com.cse.helium.app.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.util.StringUtils;

/**
 * Movie.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@JsonPropertyOrder({"id", "movieId", "partitionKey", "type", "title", "textSearch", "year",
    "runtime", "rating", "votes", "totalScore", "genres", "roles"})
public class Movie extends MovieBase {

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  @JsonIgnore
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
    // default constructor
  }

  /**
   * GetPartitionKey.
   */
  public static String getPartitionKey(String id) {
    // validate id
    if (!StringUtils.isEmpty(id) && id.length() > 5
        && StringUtils.startsWithIgnoreCase(id, "tt")) {
      int idInt = Integer.parseInt(id.substring(2));
      return String.valueOf(idInt % 10);
    }
    throw new IllegalArgumentException("Invalid Partition Key");
  }
}
