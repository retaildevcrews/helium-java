package com.microsoft.azure.helium.app.genre;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
/**
 * GenresUtils
 */
public class GenresUtils {


    public static List<Genre> generateGenres() throws IOException, ParseException {
        File file = readFile();
        ObjectMapper mapper = new ObjectMapper();
        List<Genre> mockGenres = Arrays.asList(mapper.readValue(file, Genre[].class));
        return mockGenres;
    }


    public static File readFile() throws IOException, ParseException{
        File file = new File("src/test/java/com/microsoft/azure/helium/data/genres.json");
        return file;

    }

}