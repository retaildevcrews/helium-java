package com.microsoft.cse.helium.app.models;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private String actorId;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private String name;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  private int birthYear;

  @SuppressFBWarnings("UUF_UNUSED_FIELD")
  @JsonIgnore
  public int deathYear;

  /**
   * ActorBase.
   */
  public ActorBase(){

  }
}