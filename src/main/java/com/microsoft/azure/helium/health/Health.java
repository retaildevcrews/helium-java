package com.microsoft.azure.helium.health;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Health {
    int statusCode;
    int movieCount;
    int actorCount;
    int genreCount;
}
