package com.microsoft.cse.helium.app.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

/**
 * ActorBase.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ActorBase {

  private String actorId;
  private String name;
  private int birthYear;

  @JsonIgnore
  public int deathYear;

  /**
   * ActorBase.
   */
  public ActorBase(){

  }
}