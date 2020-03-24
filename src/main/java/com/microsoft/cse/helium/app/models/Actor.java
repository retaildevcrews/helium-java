package com.microsoft.cse.helium.app.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

/**
 * Actor.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id",
    "actorId",
    "partitionKey",
    "name",
    "birthYear",
    "profession",
    "type",
    "textSearch",
    "movies"})

public class Actor extends ActorBase {
  @JsonIgnore
  private String id;

  private String partitionKey;

  private List<String> profession;
  private String type;
  private String textSearch;
  private List<MovieBase> movies;

  public Actor() {
  }

  @Override
  public String toString() {
    return "Actor{"
        + "id='" + id + '\''
        + ", partitionKey='" + partitionKey + '\''
        + ", profession=" + profession
        + ", type='" + type + '\''
        + ", textSearch='" + textSearch + '\''
        + ", movies=" + movies
        + ", deathYear=" + deathYear
        + "}";
  }
}