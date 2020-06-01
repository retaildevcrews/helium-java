package com.cse.helium.app.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class FeaturedMovie {

  private String movieId;
  private String weight;

  
  public FeaturedMovie() {
    // default constructor
  }

  @Override
  public String toString() {
    return "FeaturedMovie{" + "movieId='" + movieId + '\'' + ", weight='" + weight + '\'' + '}';
  }
}
