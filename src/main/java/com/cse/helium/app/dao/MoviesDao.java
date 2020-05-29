package com.cse.helium.app.dao;

import static com.microsoft.azure.spring.data.cosmosdb.exception.CosmosDBExceptionUtils.findAPIExceptionHandler;

import com.azure.data.cosmos.SqlParameter;
import com.azure.data.cosmos.SqlParameterList;
import com.azure.data.cosmos.SqlQuerySpec;
import com.cse.helium.app.Constants;
import com.cse.helium.app.models.Movie;
import com.cse.helium.app.services.configuration.IConfigurationService;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesDao extends BaseCosmosDbDao implements IDao {

  @Autowired GenresDao genresDao;

  private static String movieSelect =
      "select m.id, m.partitionKey, m.movieId, m.type, m.textSearch, m.title, m.year, m.runtime,"
          + "m.rating, m.votes, m.totalScore, m.genres, m.roles from m where m.type = @type  ";

  private static String movieContains = "and contains(m.textSearch, @query) ";
  private static String movieOrderBy = " order by m.textSearch, m.movieId ";
  private static String movieOffset = " offset @offset limit @limit ";

  /** MoviesDao. */
  public MoviesDao(IConfigurationService configService) {
    super(configService);
  }

  /** getMovieByIdSingleRead. */
  public Mono<Movie> getMovieById(String movieId) {
    return getContainer()
            .getItem(movieId, utils.getPartitionKey(movieId))
            .read()
            .flatMap(
                cosmosItemResponse -> Mono.justOrEmpty(cosmosItemResponse.properties().toObject(Movie.class)))
            .onErrorResume(throwable -> findAPIExceptionHandler("Failed to find item", throwable));
  }

  /**
   * This method is responsible for checking for expected values in the queryParams dictionary
   * validating them, building the query, and then passing to the base getAll() implementation.
   *
   * @param queryParams dictionary my contain "q", "year", "ratingSelect", and "actorSelect"
   * @param pageNumber used to specify which page of the paginated results to return
   * @param pageSize used to specify the number of results per page
   * @return Flux/<T/> is returned to contains results for the specific entity type
   */
  public Flux<?> getAll(Map<String, Object> queryParams, Integer pageNumber, Integer pageSize) {

    StringBuilder formedQuery = new StringBuilder(movieSelect);

    final SqlQuerySpec movieQuerySpec = new SqlQuerySpec();

    SqlParameterList parameterList = new SqlParameterList();
    parameterList.add(new SqlParameter("@type", Constants.MOVIE_DOCUMENT_TYPE));
    parameterList.add(new SqlParameter("@offset", pageNumber));
    parameterList.add(new SqlParameter("@limit", pageSize));


    if (queryParams.containsKey("q")) {
      String query = queryParams.get("q").toString();
      formedQuery.append(movieContains);
      parameterList.add(new SqlParameter("@query", query));
    }


    if (queryParams.containsKey("year")) {
      Integer year = (Integer) queryParams.get("year");
      if (year > 0) {
        formedQuery.append(" and m.year = @year");
        parameterList.add(new SqlParameter("@year", year));
      }
    }

    if (queryParams.containsKey("ratingSelect")) {
      Double rating = (Double) queryParams.get("ratingSelect");
      if (rating > 0.0) {
        formedQuery.append(" and m.rating >= @rating");
        parameterList.add(new SqlParameter("@rating", rating));
      }
    }

    if (queryParams.containsKey("actorSelect")) {
      String actorId = queryParams.get("actorSelect").toString();
      if (!StringUtils.isEmpty(actorId)) {
        formedQuery.append(" and array_contains(m.roles, { actorId: @actorId }, true) ");
        parameterList.add(new SqlParameter("@actorId", actorId));
      }
    }

    // special genre call to support webflux chain
    if (queryParams.containsKey("genre")) {
      String genre = queryParams.get("genre").toString();
      if (!StringUtils.isEmpty(genre)) {
        return filterByGenre(genre, formedQuery, parameterList);
      }
    }

    formedQuery.append(movieOrderBy).append(movieOffset);

    movieQuerySpec.queryText(formedQuery.toString());
    movieQuerySpec.parameters(parameterList);

    return super.getAll(Movie.class, movieQuerySpec);
  }

  /** filterByGenre. */
  public Flux<Movie> filterByGenre(
      String genreKey, StringBuilder formedQuery, SqlParameterList parameterList) {
    return genresDao
        .getGenreByKey(genreKey)
        .collectList()
        .flatMapMany(
            selectedGenre -> {
              formedQuery.append(" and array_contains(m.genres, @selectedGenre) ");
              parameterList.add(new SqlParameter("@selectedGenre", selectedGenre.get(0)));
              formedQuery.append(movieOrderBy).append(movieOffset);

              final SqlQuerySpec genreQuerySpec = new SqlQuerySpec();

              genreQuerySpec.queryText(formedQuery.toString());
              genreQuerySpec.parameters(parameterList);

              return super.getAll(Movie.class, genreQuerySpec);
            });
  }
}