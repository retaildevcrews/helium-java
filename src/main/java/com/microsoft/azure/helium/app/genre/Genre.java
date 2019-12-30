package com.microsoft.azure.helium.app.genre;

import com.microsoft.azure.helium.app.Constants;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Genre
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = Constants.DEFAULT_GENRE_COLLECTION_NAME)
public class Genre {

    @Id
    private String id;

    @JsonIgnore
    @PartitionKey
    private String partitionKey;
    private String type;
    private String genre;

    public Genre(){
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id='" + id + '\'' +
                ", partitionKey='" + partitionKey + '\'' +
                ", type='" + type + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}