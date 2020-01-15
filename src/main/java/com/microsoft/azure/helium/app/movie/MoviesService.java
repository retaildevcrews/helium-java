package com.microsoft.azure.helium.app.movie;

import com.azure.data.cosmos.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * MoviesService
 */
@Service
public class MoviesService {

    @Autowired
    MoviesRepository repository;

    @Autowired
    ApplicationContext context;

    private static Gson gson = new Gson();

    public List<Movie> getAllMovies(Optional<String> query,
                                              Optional<String> genre,
                                              Optional<Integer> year,
                                              Optional<Double> rating,
                                              Optional<Boolean> topRated,
                                              Optional<String> actorId,
                                              Integer pageSize,
                                              Integer pageNumber) throws CosmosClientException {

        CosmosClient client = context.getBean(CosmosClient.class);
        //String databaselink = client.getDatabaseAccount().getDatabasesLink();
        CosmosDatabase database = client.getDatabase("imdb");
        System.out.println("databaselink " + database.id());
        CosmosContainer container =  database.getContainer("movies");

        FeedOptions options = new FeedOptions();
        options.enableCrossPartitionQuery(true);

        String sql = buildCustomQuery(query, genre, year, rating, topRated, actorId, pageSize, pageNumber);
        System.out.println("query to cosmos " + sql);

        Flux<FeedResponse<CosmosItemProperties>> response =  container.queryItems(sql, options);
        List<Movie> movies = new ArrayList<>();
        response.toIterable()
                .forEach(cosmosItemFeedResponse ->{
                             for (CosmosItemProperties item : cosmosItemFeedResponse.results()) {
                                 movies.add(gson.fromJson(item.toString(), Movie.class));
                             }
                         });
        return movies;
    }

    public String buildCustomQuery(Optional<String> query,
                                   Optional<String> genre,
                                   Optional<Integer> year,
                                   Optional<Double> rating,
                                   Optional<Boolean> topRated,
                                   Optional<String> actorId,
                                   Integer pageSize,
                                   Integer pageNumber) {

        String sql = "select m.id, m.partitionKey, m.movieId, m.type, m.textSearch, m.title, m.year, m.runtime, m.rating, m.votes, m.totalScore, m.genres, m.roles from m where m.type = 'Movie' ";

        String orderBy = " order by m.title";

        int limit = pageSize;
        int offset = (pageSize * pageNumber);

        String offsetLimit = " offset " + offset + " limit " + limit;

        if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
            //  select m.movieId, m.type, m.textSearch, m.title, m.year, m.rating, m.runtime, m.genres, m.roles from m where (m.type='Movie' and contains(m.textSearch, 'talk to her')
            System.out.println("query is " + query.get().toLowerCase());
            sql += " and contains(m.textSearch,'" + query.get().toLowerCase() + "')";
        }

        if (year.isPresent() && year.get() > 0) {
            System.out.println("year is " + year.get());
            sql += " and m.year = " + year.get();
        }

        if (rating.isPresent() && rating.get() > 0) {
            System.out.println("rating is " + rating.get());
            sql += " and m.rating >= " + rating.get();
        }

        if (topRated.isPresent() && topRated.get() == true) {
            System.out.println("topRated is " + topRated.get());
            sql = "select top 10 " + sql.substring(7);
            orderBy = " order by m.rating desc";
            offsetLimit = ""; //empty the offset as results are filtered by top 10
        }

        if (actorId.isPresent() && !StringUtils.isEmpty(actorId.get())) {
            //select m.movieId, m.type, m.textSearch, m.title, m.year, m.runtime, m.genres, m.roles from m where array_contains(m.roles,{ actorId: 'nm0000704',true})
            System.out.println("actorId is " + actorId.get());
            // get movies for an actor
            sql += " and array_contains(m.roles, { actorId: '";
            sql += actorId.get();
            sql += "' }, true) ";

        }

        if (genre.isPresent() && !StringUtils.isEmpty(genre.get())) {
            System.out.println("genre is " + genre.get());
            // get movies by genre
            //select m.movieId, m.type, m.textSearch, m.title, m.year, m.runtime, m.genres, m.roles from m where array_contains(m.genres,'Romance')
            String genreVal = StringUtils.capitalize(genre.get());
            System.out.println("genreVal " + genreVal);
            String genreQuery = " and array_contains(m.genres,'" + genreVal + "')";
            sql += genreQuery;
        }

        sql += orderBy + offsetLimit;
        return sql;
    }


    /*This API uses QueryDocument with PartitionKey */
    public Optional<Movie> getMovie(String movieId) {
        if (StringUtils.isEmpty(movieId)) {
            throw new NullPointerException("movieId cannot be empty or null");
        }
        //queries by partitionid - partitionkey is the field annotated with @partitionkey
        List<Movie> movies = repository.findByMovieId(movieId);
        //queries without partitionkey
        //repository.findById(movieId);
        if (movies.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(movies.get(0));
        }
    }

}



