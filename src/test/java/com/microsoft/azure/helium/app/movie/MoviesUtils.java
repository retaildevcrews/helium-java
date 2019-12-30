package com.microsoft.azure.helium.app.movie;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.helium.app.actor.Actor;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

/**
 * MoviesUtils
 */
public class MoviesUtils {


    public static List<Movie> generateMovies() throws IOException, ParseException {
        File file = readFile();
        ObjectMapper mapper = new ObjectMapper();
        List<Movie> mockMovies = Arrays.asList(mapper.readValue(file, Movie[].class));
        return mockMovies;
    }

    public static Movie generateMovieWithId() throws IOException, ParseException {
        List<Movie> movies =  generateMovies();
        return movies.get(0);
    }

    public static String expectedMovieResponse() throws IOException, ParseException{
        File file = readFile();
        FileReader reader = new FileReader(file);
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        return obj.toString();
    }

    public static File readFile() throws IOException, ParseException{
        File file = new File("src/test/java/com/microsoft/azure/helium/data/movies.json");
        return file;

    }
}