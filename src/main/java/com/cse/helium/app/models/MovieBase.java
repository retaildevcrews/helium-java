package com.cse.helium.app.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Movie.
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

  public MovieBase() {
    // default constructor
  }

}