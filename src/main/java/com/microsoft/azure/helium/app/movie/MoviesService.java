package com.microsoft.azure.helium.app.movie;

import com.azure.data.cosmos.*;
import com.google.gson.Gson;
import com.microsoft.azure.helium.app.Constants;
import com.microsoft.azure.helium.app.genre.Genre;
import com.microsoft.azure.helium.app.genre.GenresRepository;
import com.microsoft.azure.helium.app.genre.GenresService;
import com.microsoft.azure.helium.config.BuildConfig;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
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

    private static final Logger logger = LoggerFactory.getLogger(BuildConfig.class);

    @Autowired
    MoviesRepository repository;

    @Autowired
    GenresService genresService;

    @Autowired
    GenresRepository genresRepository;

    @Autowired
    ApplicationContext context;

    private static Gson gson = new Gson();

    public List<Movie> getAllMovies(Optional<String> query,
                                              Optional<String> genre,
                                              Optional<Integer> year,
                                              Optional<Double> rating,
                                              Optional<Boolean> topRated,
                                              Optional<String> actorId,
                                              Optional<Integer> pageSize,
                                              Optional<Integer> pageNumber) throws CosmosClientException {

        CosmosClient client = context.getBean(CosmosClient.class);
        CosmosDatabase database = client.getDatabase("imdb");
        logger.info("databaselink " + database.id());
        CosmosContainer container =  database.getContainer("movies");

        FeedOptions options = new FeedOptions();
        options.enableCrossPartitionQuery(true);

        String sql = buildCustomQuery(query, genre, year, rating, topRated, actorId, pageSize, pageNumber);
        logger.info("query to cosmos " + sql);

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
                                   Optional<Integer> pageSize,
                                   Optional<Integer> pageNumber) {

        String sql = "select m.id, m.partitionKey, m.movieId, m.type, m.textSearch, m.title, m.year, m.runtime, m.rating, m.votes, m.totalScore, m.genres, m.roles from m where m.type = 'Movie' ";
        String orderBy = " order by m.title";

        Integer limit = 0;
        Integer offset = 0;

        try {
            if (pageSize.isPresent() && pageSize.get() >= 0) {
                limit = pageSize.get();
            }
            if (limit < 1) {
                limit = Constants.DefaultPageSize;
            } else if (limit > Constants.MaxPageSize) {
                limit = Constants.MaxPageSize;
            }

            if (pageNumber.isPresent() && pageNumber.get() > 0) {
                offset = (limit * pageNumber.get());
            }

            String offsetLimit = " offset " + offset + " limit " + limit;

            if (query.isPresent() && !StringUtils.isEmpty(query.get())) {
                //  select m.movieId, m.type, m.textSearch, m.title, m.year, m.rating, m.runtime, m.genres, m.roles from m where (m.type='Movie' and contains(m.textSearch, 'talk to her')
                sql += " and contains(m.textSearch,'" + query.get().toLowerCase() + "')";
            }

            if (year.isPresent() && year.get() > 0) {
                sql += " and m.year = " + year.get();
            }

            if (rating.isPresent() && rating.get() > 0) {
                sql += " and m.rating >= " + rating.get();
            }

            if (topRated.isPresent() && topRated.get() == true) {
                sql = "select top 10 " + sql.substring(7);
                orderBy = " order by m.rating desc";
                offsetLimit = ""; //empty the offset as results are filtered by top 10
            }

            if (actorId.isPresent() && !StringUtils.isEmpty(actorId.get())) {
                //select m.movieId, m.type, m.textSearch, m.title, m.year, m.runtime, m.genres, m.roles from m where array_contains(m.roles,{ actorId: 'nm0000704',true})
                // get movies for an actor
                sql += " and array_contains(m.roles, { actorId: '";
                sql += actorId.get();
                sql += "' }, true) ";

            }

            if (genre.isPresent() && !StringUtils.isEmpty(genre.get())) {
                Optional<Genre> genreResp = genresRepository.findById(genre.get());
                // get movies by genre
                String genreQuery = " and array_contains(m.genres,'" + genreResp.get().getGenre() + "')";
                sql += genreQuery;
            }

            sql += orderBy + offsetLimit;
        }catch (Exception ex){
            logger.error("Exception thrown " + ex.getMessage());
        }
        return sql;
    }


    /*This API uses QueryDocument with PartitionKey */
    public Optional<Movie> getMovie(String movieId) {
        if (StringUtils.isEmpty(movieId)) {
            throw new NullPointerException("movieId cannot be empty or null");
        }
        //queries by partitionid - partitionkey is the field annotated with @partitionkey
        List<Movie> movies = repository.findByMovieId(movieId);
        if (movies.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(movies.get(0));
        }
    }

}



