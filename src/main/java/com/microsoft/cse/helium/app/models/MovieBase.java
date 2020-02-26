package com.microsoft.cse.helium.app.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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