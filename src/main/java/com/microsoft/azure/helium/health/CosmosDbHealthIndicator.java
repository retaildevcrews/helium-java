package com.microsoft.azure.helium.health;

import com.microsoft.azure.documentdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * CosmosDbHealthIndicator
 */
@Component
public class CosmosDbHealthIndicator extends AbstractHealthIndicator {

	@Autowired
	private DocumentClient documentClient;

	@Autowired
	private BuildProperties buildProperties;

	private String dbName;

	public CosmosDbHealthIndicator(@Value("${azure.cosmosdb.database}") String dbName) {
		super();
		this.dbName = dbName;
	}

	@Override
	protected void doHealthCheck(Builder builder) throws DocumentClientException {

		try {

			HashMap<String, Long> result = getStatusCode(dbName);
			Long statusCode = result.get("status");
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


	protected HashMap<String, Long > getStatusCode(String dbName) throws DocumentClientException {

		ResourceResponse<Database> response = this.documentClient.readDatabase("dbs/" + dbName, new RequestOptions());
		HashMap<String, Long> resultDetails = new HashMap<>();

		FeedOptions queryOptions = new FeedOptions();
		queryOptions.setPageSize(-1);
		queryOptions.setEnableCrossPartitionQuery(true);


		resultDetails.put("status", Long.valueOf(response.getStatusCode()));
		String collectionMoviesLink = String.format("/dbs/%s/colls/%s", "imdb","movies");
		List<Document> moviesResult =  documentClient.queryDocuments(collectionMoviesLink, "SELECT VALUE COUNT(1) FROM c", queryOptions).getQueryIterable().toList();
		Long movieCount = (Long) moviesResult.get(0).get("_aggregate");
		resultDetails.put("movies", movieCount);

		String collectionActorsLink = String.format("/dbs/%s/colls/%s", "imdb","actors");
		List<Document> actorsResult =  documentClient.queryDocuments(collectionActorsLink, "SELECT VALUE COUNT(1) FROM c", queryOptions).getQueryIterable().toList();
		Long actorCount = (Long) actorsResult.get(0).get("_aggregate");
		resultDetails.put("actors", actorCount);

		String collectionGenresLink = String.format("/dbs/%s/colls/%s", "imdb","genres");
		List<Document> genresResult =  documentClient.queryDocuments(collectionGenresLink, "SELECT VALUE COUNT(1) FROM c", queryOptions).getQueryIterable().toList();
		Long genreCount = (Long) genresResult.get(0).get("_aggregate");
		resultDetails.put("genres", genreCount);
		resultDetails.put("version", buildProperties.getTime().getEpochSecond());
		return resultDetails;
	}

}