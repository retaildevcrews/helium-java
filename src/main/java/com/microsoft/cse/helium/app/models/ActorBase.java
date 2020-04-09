package com.microsoft.cse.helium.app.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
  @JsonInclude(Include.NON_DEFAULT)
  public int deathYear;

  /**
   * ActorBase.
   */
  public ActorBase(){

  }
}