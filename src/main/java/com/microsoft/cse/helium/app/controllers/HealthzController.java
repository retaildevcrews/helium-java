package com.microsoft.cse.helium.app.controllers;

import com.azure.data.cosmos.CosmosClientException;
import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.config.BuildConfig;
import com.microsoft.cse.helium.app.dao.ActorsDao;
import com.microsoft.cse.helium.app.dao.GenresDao;
import com.microsoft.cse.helium.app.dao.MoviesDao;
import com.microsoft.cse.helium.app.health.ietf.IeTfStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/healthz")
public class HealthzController {

  private static final Logger logger = LoggerFactory.getLogger(HealthzController.class);

  @Autowired private BuildConfig buildConfig;

  @Autowired Environment environment;

  @Autowired GenresDao genresDao;

  @Autowired MoviesDao moviesDao;

  @Autowired ActorsDao actorsDao;

  // Used to update start time to calculate duration.
  // Using a list, because the variable must be final when used in the map.
  final List<Long> timeList = new ArrayList<Long>();

  /**
   * healthCheck.
   *
   * @return
  */
  @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<ResponseEntity<String>> healthCheck() throws CosmosClientException {
    logger.info("healthz endpoint");

    Mono<List<Map<String, String>>> resultsMono = buildHealthCheckChain();

    return resultsMono.map(data -> {
      String healthStatus = getOverallHealthStatus(data);
      int resCode = healthStatus.equals(IeTfStatus.fail.name())
          ? HttpStatus.SERVICE_UNAVAILABLE.value()
          : HttpStatus.OK.value();
      return new ResponseEntity<String>(healthStatus, 
          null,
          HttpStatus.valueOf(resCode));
    });
  }
  
  /**
   * ietfhealthCheck runs several checks and returs and overall status and results for
   *    the discrete calls that are made.  
   *
   *    @return Mono{@literal <}ResponseEntity{@literal <}LinkedHashMap{@literal <}String, 
   *        Object{@literal <}{@literal <}{@literal <}
   *        returned with status information for overall execution and discrete calls.
   */
  @GetMapping(value = "/ietf", produces = "application/health+json")
  public Mono<ResponseEntity<LinkedHashMap<String, Object>>>  ietfHealthCheck() 
      throws CosmosClientException {
    logger.info("healthz ietf endpoint");

    LinkedHashMap<String, Object> ieTfResult = new LinkedHashMap<>();

    String webInstanceRole = environment.getProperty(Constants.webInstanceRole);
    if (webInstanceRole == null || webInstanceRole.isEmpty()) {
      webInstanceRole = "unknown";
    }

    ieTfResult.put("serviceId", "helium-java");
    ieTfResult.put("description", "Helium Java Health Check");
    ieTfResult.put("instance", webInstanceRole);
    ieTfResult.put("version", buildConfig.getBuildVersion());

    Mono<List<Map<String, String>>> resultsMono = buildHealthCheckChain();

    return resultsMono.map(data -> {
      ieTfResult.put("status", getOverallHealthStatus(data));
      Map<String, Object> resultsDictionary = convertResultsListToDictionary(data);

      ieTfResult.put("checks", resultsDictionary);
      return ieTfResult;
    }).map(result -> ResponseEntity.ok().body(result));
  }

  /** buildHelthCheckChain build the chain of calls using concatWith. */
  Mono<List<Map<String, String>>> buildHealthCheckChain() {
    timeList.add(System.currentTimeMillis());
    /*  build discrete API calls   */
    Mono<Map<String,String>> genreMono = genresDao.getGenres()
        .map(genre -> {
          //seed the time list
          return buildResultsDictionary("getGenres", getElapsedAndUpdateStart(), 400L);
        });

    Mono<Map<String,String>> actorByIdMono = actorsDao.getActorById("nm0000173")
        .map(actor -> {
          return buildResultsDictionary("getActorById", getElapsedAndUpdateStart(), 250L);
        });

    Mono<Map<String, String>> movieByIdMono = moviesDao.getMovieById("tt0133093")
        .map(movie -> {
          return buildResultsDictionary("getMovieById", getElapsedAndUpdateStart(), 250L);
        });    

    Map<String, Object> moviesQueryParams = new HashMap<>();
    moviesQueryParams.put("q", "ring");
    Mono<Map<String, String>> moviesQueryMono = 
        moviesDao.getAll(moviesQueryParams, 1, 100)
        .collectList()
        .map(results -> {
          return buildResultsDictionary("searchMovies", getElapsedAndUpdateStart(), 400L);
        });

    Map<String, Object> actorsQueryParams = new HashMap<>();
    actorsQueryParams.put("q", "nicole");
    Mono<Map<String,String>> actorsQueryMono =
        actorsDao.getAll(actorsQueryParams, 1, 100)
        .collectList()
        .map(results -> {
          return buildResultsDictionary("searchActors", getElapsedAndUpdateStart(), 400L);
        });
    /*   chain the discrete calls together   */
    Mono<List<Map<String, String>>> resultsMono =  genreMono.concatWith(actorByIdMono)
        .concatWith(movieByIdMono)    
        .concatWith(moviesQueryMono)
        .concatWith(actorsQueryMono).collectList();

    return resultsMono;
  }

  Long getElapsedAndUpdateStart() {
    Long start = timeList.get(timeList.size() - 1);
    Long elapsed = System.currentTimeMillis() - start;
    timeList.add(System.currentTimeMillis());
    return elapsed;
  }

  String getOverallHealthStatus(List<Map<String, String>> resultsList) {
    String returnStatus = IeTfStatus.pass.name();

    for (Map<String, String> resultItem : resultsList) {
      if (!resultItem.get("status").toLowerCase().equals(IeTfStatus.pass.name())) {
        returnStatus = resultItem.get("status");
        if (returnStatus.equals(IeTfStatus.fail.name())) {
          // if we hit a fail then break otherwise loop to end
          break;
        }
      } 
    }
    return returnStatus;
  }
  
  /** convertResultsListToDictionary converts the list from the chain to a dictionary. */
  Map<String, Object> convertResultsListToDictionary(List<Map<String, String>> resultsList) {
    Map<String, Object> returnDict = new HashMap<String, Object>();

    resultsList.forEach(resultItem -> {
      String keyName = resultItem.get("componentId") + ":responseTime";
      returnDict.put(keyName,resultItem);
    });

    return returnDict;
  }

  /** buildResultsDictionary used to create the discrete results for each call in the chain. */
  Map<String, String> buildResultsDictionary(String componentId, 
      Long duration, Long expectedDuration) {
    String passStatus = IeTfStatus.fail.name();
    if (duration <= expectedDuration) {
      passStatus = IeTfStatus.pass.name();
    } else {
      passStatus = IeTfStatus.warn.name();
    }

    Map<String, String> resultsDict = new HashMap<String, String>();
    resultsDict.put("componentId", componentId);
    resultsDict.put("componentType", "datastore");
    resultsDict.put("observedUnit", "ms");
    resultsDict.put("observedValue", Long.toString(duration));
    resultsDict.put("status", passStatus);
    resultsDict.put("targetValue", Long.toString(expectedDuration));
    resultsDict.put("time",  new Date().toInstant().toString());

    return resultsDict;
  }
}
