package com.cse.helium.app.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"order", "category", "character"})
// suppressing requirement for static final and accessors as this matches data
@SuppressWarnings ("squid:S1104") 
public class Role extends ActorBase {

  public int order;
  public String category;

  @JsonInclude(Include.NON_DEFAULT)
  public List<String> characters;

  public Role() {
    // default constructor
  }
}
