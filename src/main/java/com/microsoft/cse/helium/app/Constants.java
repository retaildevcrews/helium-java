package com.microsoft.cse.helium.app;

/**
 * Constants.
 */
public final class Constants {
  public static final String DEFAULT_GENRE_COLLECTION_NAME = "genres";
  public static final String DEFAULT_ACTOR_COLLECTION_NAME = "actors";
  public static final String DEFAULT_MOVIE_COLLECTION_NAME = "movies";
  public static final String DEFAULT_FEATURED_COLLECTION_NAME = "featured";
  /* ================================================== */

  public static final String COSMOS_URL_KEYNAME = "CosmosUrl";
  public static final String COSMOS_KEY_KEYNAME = "CosmosKey";
  public static final String COSMOS_DATABASE_KEYNAME = "CosmosDatabase";
  public static final String COSMOS_COLLECTION_KEYNAME = "CosmosCollection";

  public static final Integer DEFAULT_PAGE_SIZE = 100;
  public static final Integer MAX_PAGE_SIZE = 1000;
  public static final Integer MAX_PAGE_COUNT = 1000;
  public static final String WEB_INSTANCE_ROLE = "WEBSITE_ROLE_INSTANCE_ID";
  public static final Integer MAX_DEGREE_PARALLELISM = 2;
}
