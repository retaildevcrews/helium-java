package com.microsoft.azure.helium.app.movie;

import com.microsoft.azure.helium.app.Constants;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Movie
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode

public class MovieBase {

  private String movieId;
  private String type;
  private String title;
  private int year;
  public int runtime;
  private List<String> genres;

  public  MovieBase(){}

}