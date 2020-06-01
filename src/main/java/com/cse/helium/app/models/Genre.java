package com.cse.helium.app.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Genre {

  @SuppressWarnings("squid:S1700") // suppress rename field as this matches data
  private String genre;

  public Genre() {
    // default constructor
  }
}
