package com.microsoft.cse.helium.app.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder({"order" , "category" , "character"})
public class Role extends ActorBase {

    public int order;
    public String category;
    public List<String> characters;

    public Role(){
    }
}
