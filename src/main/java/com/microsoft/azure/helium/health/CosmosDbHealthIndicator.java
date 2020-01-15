package com.microsoft.azure.helium.health;

import com.azure.data.cosmos.*;
import com.microsoft.azure.helium.app.movie.Movie;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * CosmosDbHealthIndicator
 */
@Component
public class CosmosDbHealthIndicator extends AbstractHealthIndicator {

	@Autowired
	private CosmosClient documentClient;

	@Autowired
	private BuildProperties buildProperties;

	private String dbName;

	public CosmosDbHealthIndicator(@Value("${azure.cosmosdb.database}") String dbName) {
		super();
		this.dbName = dbName;
	}

	@Override
	protected void doHealthCheck(org.springframework.boot.actuate.health.Health.Builder builder) throws Exception {
		try {

			HashMap<String, Integer> result = getStatusCode(dbName);
			Integer statusCode = result.get("status");
			result.forEach((key,value) -> System.out.println(key + " = " + value));
			if (HttpStatus.valueOf((int) (long) statusCode).is2xxSuccessful()) {
				builder.up().withDetails(result);
			} else {
				builder.down().withDetail("error code", statusCode).build();
			}
		} catch (Exception ex) {
			builder.down().withDetail("error", ex.getMessage());
		}
	}


	protected HashMap<String, Integer > getStatusCode(String dbName) throws CosmosClientException {

		//ResourceResponse<Database> response = this.documentClient.readDatabase("dbs/" + dbName, new RequestOptions());
		System.out.println("databaselink " + documentClient.getDatabase(dbName).id());
		CosmosContainer container =  documentClient.getDatabase(dbName).getContainer("movies");

		CosmosDatabaseResponse response = documentClient.getDatabase(dbName).read().block();
		HashMap<String, Integer> resultDetails = new HashMap<>();
		FeedOptions queryOptions = new FeedOptions();
		//queryOptions.setPageSize(-1);
		queryOptions.enableCrossPartitionQuery(true);


		resultDetails.put("status", response.statusCode());
		String collectionMoviesLink = String.format("/dbs/%s/colls/%s", "imdb","movies");

		Flux<FeedResponse<CosmosItemProperties>> movieResponse =  documentClient.getDatabase(dbName).getContainer("movies").queryItems("SELECT VALUE COUNT(1) FROM c", queryOptions);
		//TODO check the query response for NPE
		Integer movieCount = (Integer)  movieResponse.blockLast().results().get(0).get("_aggregate");
		resultDetails.put("movies", movieCount);

		Flux<FeedResponse<CosmosItemProperties>> actorResponse =  documentClient.getDatabase(dbName).getContainer("actors").queryItems("SELECT VALUE COUNT(1) FROM c", queryOptions);
		//TODO check the query response for NPE
		Integer actorCount = (Integer)  actorResponse.blockLast().results().get(0).get("_aggregate");
		resultDetails.put("actors", actorCount);

		Flux<FeedResponse<CosmosItemProperties>> genreResponse =  documentClient.getDatabase(dbName).getContainer("genres").queryItems("SELECT VALUE COUNT(1) FROM c", queryOptions);
		//TODO check the query response for NPE
		Integer genreCount = (Integer)   actorResponse.blockLast().results().get(0).get("_aggregate");
		resultDetails.put("genres", genreCount);

		Long x = buildProperties.getTime().getEpochSecond();
		int  y = x.intValue();
		resultDetails.put("version", y);
		return resultDetails;
	}

}