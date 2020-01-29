package com.microsoft.azure.helium.app;

import com.azure.data.cosmos.*;
import com.microsoft.azure.helium.app.actor.ActorsService;
import com.microsoft.azure.helium.app.genre.GenresService;
import com.microsoft.azure.helium.app.movie.MoviesService;
import com.microsoft.azure.helium.config.BuildConfig;
import com.microsoft.azure.helium.health.ietf.IeTfStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(path = "/healthz")
public class HealthzController {

    @Autowired
    private MoviesService moviesService;

    @Autowired
    private ActorsService actorsService;

    @Autowired
    private GenresService genresService;

    @Autowired
    private BuildConfig buildConfig;

    @Autowired
    Environment environment;

    @GetMapping(path="", produces = "text/plain")
    public  ResponseEntity<String> healthCheck(HttpServletResponse response) throws CosmosClientException {
        HashMap<String, Object> healthCheckResult = runHealthChecks();
        int resCode = healthCheckResult.get("status") == IeTfStatus.fail.name() ? HttpStatus.SERVICE_UNAVAILABLE.value() : HttpStatus.OK.value();
        String status = healthCheckResult.get("status").toString();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<String>(status, responseHeaders, HttpStatus.valueOf(resCode));

    }


    @GetMapping(path="/ietf", produces = "application/health+json")
    public ResponseEntity<HashMap<String, Object>> ietfHealthCheck(HttpServletResponse response) throws CosmosClientException {
        HashMap<String, Object> healthCheckResult = runHealthChecks();
        int resCode = healthCheckResult.get("status") == IeTfStatus.fail.name() ? HttpStatus.SERVICE_UNAVAILABLE.value(): HttpStatus.OK.value();
        response.setHeader("Content-Type", "application/health+json");
        return new ResponseEntity<HashMap<String, Object>>(healthCheckResult, HttpStatus.valueOf(resCode));

    }

    private  HashMap<String, Object> runHealthChecks() throws CosmosClientException {

        String webInstanceRole = environment.getProperty(Constants.webInstanceRole);
        if(webInstanceRole == null || webInstanceRole.isEmpty()){
            webInstanceRole = "unknown";
        }
        HashMap<String, Object> ieTfResult = new HashMap<>();
        ieTfResult.put("status", IeTfStatus.pass.name());
        ieTfResult.put("serviceId", "helium-java");
        ieTfResult.put("description", "helium-java");
        ieTfResult.put("version", buildConfig.getBuildVersion());
        ieTfResult.put("instance", webInstanceRole);

        HashMap<String, HashMap<String, String>> healthChecks = new HashMap<>();
        try {
            healthChecks.put("getGenres", runHealthChecksOnEndpoints("/api/genres", 400));
            healthChecks.put("getActorById", runHealthChecksOnEndpoints("/api/actors/nm0000173", 250));
            healthChecks.put("getMovieById", runHealthChecksOnEndpoints("/api/movies/tt0133093", 250));
            healthChecks.put("searchMovies", runHealthChecksOnEndpoints("/api/movies?q=ring", 400));
            healthChecks.put("searchActors", runHealthChecksOnEndpoints("/api/actors?q=nicole", 400));
            healthChecks.put("getTopRatedMovie", runHealthChecksOnEndpoints("/api/movies?toprated=true", 400));

            for(Map.Entry<String, HashMap<String, String>> entry : healthChecks.entrySet()) {
                HashMap check = entry.getValue();
                if (check.containsKey(IeTfStatus.pass.name())) {
                    ieTfResult.put("status", IeTfStatus.pass.name());
                }
                if (check.containsKey(IeTfStatus.fail.name())) {
                    break;
                }
            }

            ieTfResult.put("checks", healthChecks);
            return ieTfResult;

        }catch (Exception ex) {
            System.out.println("Error is " + ex.getMessage());
            ieTfResult.put("status", IeTfStatus.fail.name());
            ieTfResult.put("cosmosException", ex.getMessage());
            ieTfResult.put("checks", healthChecks);
            return ieTfResult;
        }
    }

    private HashMap<String, String> runHealthChecksOnEndpoints(String endpoint, Integer target) throws CosmosClientException {
        Date start = new Date();

        if (endpoint == "/api/genres") {
            genresService.getAllGenres();
        }else if (endpoint == "/api/actors/nm0000173") {
            String [] parts = endpoint.split("/");
            actorsService.getActor( parts[3]);
        }else if (endpoint == "/api/movies/tt0133093") {
            String [] parts = endpoint.split("/");
            moviesService.getMovie(parts[3]);
        }else if (endpoint == "/api/movies?q=ring") {
            String [] parts = endpoint.split("=");
            moviesService.getAllMovies(java.util.Optional.of(parts[1]), null, (java.util.Optional.of(0)), (java.util.Optional.of(0.0)),(java.util.Optional.of(Boolean.FALSE)), null, (java.util.Optional.of(100)), (java.util.Optional.of(0)));
        }else if (endpoint == "/api/actors?q=nicole") {
            String [] parts = endpoint.split("=");
            actorsService.getAllActors(java.util.Optional.of(parts[1]),  (java.util.Optional.of(0)),  (java.util.Optional.of(100)), null);
        }else{
            moviesService.getAllMovies(null, null, (java.util.Optional.of(0)), (java.util.Optional.of(0.0)),(java.util.Optional.of(Boolean.TRUE)), null, (java.util.Optional.of(100)), (java.util.Optional.of(1)));
        }

        Long duration = new Date().getTime() - start.getTime();

        HashMap<String, String> healthCheckResult = new HashMap<>();
        healthCheckResult.put("status", IeTfStatus.pass.name());
        healthCheckResult.put("componentType", "CosmosDB");
        healthCheckResult.put("observedUnit", "ms");
        healthCheckResult.put("observedValue", duration.toString());
        healthCheckResult.put("targetValue", target.toString());
        healthCheckResult.put("time", start.toInstant().toString());
        healthCheckResult.put("affectedEndpoints", endpoint);

        if (duration > target) {
            healthCheckResult.put("status", IeTfStatus.warn.name());
            healthCheckResult.put("message", "Request exceeded expected duration");
        }

        return healthCheckResult;

    }
}
