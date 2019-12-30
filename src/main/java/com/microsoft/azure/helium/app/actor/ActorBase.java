package com.microsoft.azure.helium.app.actor;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.azure.helium.app.Constants;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Actor
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

    public ActorBase() {

    }
}