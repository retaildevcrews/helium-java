package com.microsoft.azure.helium.app.actor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.microsoft.azure.helium.app.Constants;

import com.microsoft.azure.helium.app.movie.MovieBase;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Actor
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = Constants.DEFAULT_ACTOR_COLLECTION_NAME)

@JsonPropertyOrder({"id","actorId", "partitionKey",  "name", "birthYear", "profession", "type", "textSearch", "movies"})

public class Actor extends ActorBase{
    @JsonIgnore
    @Id
    private String id;

    @PartitionKey
    private String partitionKey;

    private List<String> profession;
    private String type;
    private String textSearch;
    private List<MovieBase> movies;


    public Actor(){
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id='" + id + '\'' +
                ", partitionKey='" + partitionKey + '\'' +
                ", profession=" + profession +
                ", type='" + type + '\'' +
                ", textSearch='" + textSearch + '\'' +
                ", movies=" + movies +
                ", deathYear=" + deathYear +
                '}';
    }
}