package com.microsoft.cse.helium.app.controllers;

import com.azure.data.cosmos.CosmosClientException;
import com.microsoft.cse.helium.app.Constants;
import com.microsoft.cse.helium.app.config.BuildConfig;
import com.microsoft.cse.helium.app.dao.ActorsDao;
import com.microsoft.cse.helium.app.dao.GenresDao;
import com.microsoft.cse.helium.app.dao.MoviesDao;
//import com.microsoft.cse.helium.app.health.ietf.IeTfStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
//import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
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
  /*
   * healthCheck.
   *
   * @return
   */
  /*
   @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity healthCheck() throws CosmosClientException {
    logger.info("healthz endpoint");

    Mono<List<Map<String, String>>> resultsMono = buildHealthCheckChain();

    return resultsMono.map(data -> {
      String healthStatus = getOverallHealthStatus(data);

      ieTfResult.put("checks", resultsDictionary);
      return ieTfResult;
    }).map(result -> ResponseEntity.ok().body(result));

    int resCode =
        healthCheckResult.get("status").equals(IeTfStatus.fail.name())
            ? HttpStatus.SERVICE_UNAVAILABLE.value()
            : HttpStatus.OK.value();
    String status = healthCheckResult.get("status").toString();
    System.out.println("healthCheckResult status " + status);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
    ResponseEntity output =
        new ResponseEntity<String>(status, responseHeaders, HttpStatus.valueOf(resCode));
    return output;
  }
  */
  
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
    String returnStatus = "pass";

    for (Map<String, String> resultItem : resultsList) {
      if (!resultItem.get("status").toLowerCase().equals("pass")) {
        returnStatus = resultItem.get("status");
        if (returnStatus.equals("fail")) {
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
    //TODO: Convert to use ietf status enum
    String passStatus = "fail";
    if (duration <= expectedDuration) {
      passStatus = "pass";
    } else {
      passStatus = "warn";
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
  
  /*
  private HashMap<String, Object> runHealthChecks() throws CosmosClientException {

    String webInstanceRole = environment.getProperty(Constants.webInstanceRole);
    if (webInstanceRole == null || webInstanceRole.isEmpty()) {
      webInstanceRole = "unknown";
    }

    LinkedHashMap<String, Object> ieTfResult = new LinkedHashMap<>();
    ieTfResult.put("status", IeTfStatus.pass.name());
    ieTfResult.put("serviceId", "helium-java");
    ieTfResult.put("description", "Helium Java Health Check");
    ieTfResult.put("instance", webInstanceRole);
    ieTfResult.put("version", buildConfig.getBuildVersion());

    LinkedHashMap<String, LinkedHashMap<String, Object>> healthChecks = new LinkedHashMap<>();
    try {
      healthChecks.put(
          "getGenres:responseTime", runHealthChecksOnEndpoints("getGenres", "/api/genres", 400L));
      healthChecks.put(
          "getActorById:responseTime",
          runHealthChecksOnEndpoints("getActorById", "/api/actors/nm0000173", 250L));
      healthChecks.put(
          "getMovieById:responseTime",
          runHealthChecksOnEndpoints("getMovieById", "/api/movies/tt0133093", 250L));
      healthChecks.put(
          "searchMovies:responseTime",
          runHealthChecksOnEndpoints("searchMovies", "/api/movies?q=ring", 400L));
      healthChecks.put(
          "searchActors:responseTime",
          runHealthChecksOnEndpoints("searchActors", "/api/actors?q=nicole", 400L));

      // if any health check has a warn or down status -  set overall status to the worst status
      for (Map.Entry<String, LinkedHashMap<String, Object>> entry : healthChecks.entrySet()) {
        LinkedHashMap check = entry.getValue();
        if (!check.containsValue(IeTfStatus.pass.name())) {
          ieTfResult.put("status", check.get("status"));
        }
        if (check.containsValue(IeTfStatus.fail.name())) {
          break;
        }
      }

      ieTfResult.put("checks", healthChecks);
      return ieTfResult;

    } catch (Exception ex) {
      System.out.println("CosmosException: Healthz:" + ex.getMessage());
      ieTfResult.put("status", IeTfStatus.fail.name());
      ieTfResult.put("cosmosException", ex.getMessage());
      ieTfResult.put("checks", healthChecks);
      return ieTfResult;
    }
  }

  private LinkedHashMap<String, Object> runHealthChecksOnEndpoints(
      String componentId, String endpoint, Long target) throws CosmosClientException {

    final Date date = new Date();
    long start = System.currentTimeMillis();

    LinkedHashMap<String, Object> healthCheckResult = new LinkedHashMap<>();
    ArrayList<String> endPoints = new ArrayList<String>();
    endPoints.add(endpoint);

    healthCheckResult.put("status", IeTfStatus.pass.name());
    healthCheckResult.put("componentId", componentId);
    healthCheckResult.put("componentType", "datastore");
    healthCheckResult.put("observedUnit", "ms");
    healthCheckResult.put("observedValue", 0);
    healthCheckResult.put("targetValue", target);
    healthCheckResult.put("time", date.toInstant().toString());

    try {
      if (endpoint.equalsIgnoreCase("/api/genres")) {
        genresDao.getGenres();
      } else if (endpoint.equalsIgnoreCase("/api/actors/nm0000173")) {
        String[] parts = endpoint.split("/");
        actorsDao.getActorById(parts[3]);
      } else if (endpoint.equalsIgnoreCase("/api/movies/tt0133093")) {
        String[] parts = endpoint.split("/");
        moviesDao.getMovieById(parts[3]);
      } else if (endpoint.equalsIgnoreCase("/api/movies?q=ring")) {
        String[] parts = endpoint.split("=");
        Map<String, Object> moviesQueryParams = new HashMap<>();
        moviesQueryParams.put("q", "ring");
        moviesDao.getAll(moviesQueryParams, 1, 100);
      } else if (endpoint.equalsIgnoreCase("/api/actors?q=nicole")) {
        String[] parts = endpoint.split("=");
        Map<String, Object> actorsQueryParams = new HashMap<>();
        actorsQueryParams.put("q", "nicole");
        actorsDao.getAll(actorsQueryParams, 1, 100);
      } else {
        Map<String, Object> moviesQueryParams = new HashMap<>();
        moviesDao.getAll(moviesQueryParams, 1, 100);
      }
      healthCheckResult.put("observedValue", System.currentTimeMillis() - start);

    } catch (Exception ex) {

      healthCheckResult.put("observedValue", System.currentTimeMillis() - start);
      healthCheckResult.put("status", IeTfStatus.fail.name());
      healthCheckResult.put("targetValue", target);
      healthCheckResult.put("time", date.toInstant().toString());
      healthCheckResult.put("affectedEndpoints", endPoints);
      healthCheckResult.put("message", ex.getMessage());
      throw ex;
    }

    if ((Long) healthCheckResult.get("observedValue")
        > (Long) healthCheckResult.get("targetValue")) {
      healthCheckResult.put("status", IeTfStatus.warn.name());
      healthCheckResult.put("affectedEndpoints", endPoints);
      healthCheckResult.put("message", "Request exceeded expected duration");
    }
    return healthCheckResult;
  }
  */
}
