package com.microsoft.azure.helium.app.actor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.helium.app.movie.Movie;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

/**
 * ActorsUtils
 */
public class ActorsUtils {

    public static List<Actor> generateActors() throws IOException, ParseException {
        File file = readFile();
        ObjectMapper mapper = new ObjectMapper();
        List<Actor> mockActors = Arrays.asList(mapper.readValue(file, Actor[].class));
        return mockActors;
    }

    public static Actor generateActorWithId() throws IOException, ParseException {
        List<Actor> actors =  generateActors();
        return actors.get(0);
    }

    public static String expectedActorResponse() throws IOException, ParseException{
        File file = readFile();
        FileReader reader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        return obj.toString();
    }

    public static File readFile() throws IOException, ParseException{
        File file = new File("src/test/java/com/microsoft/azure/helium/data/actors.json");
        return file;

    }


}