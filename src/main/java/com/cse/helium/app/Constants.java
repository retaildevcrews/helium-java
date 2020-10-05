package com.cse.helium.app;

/**
 * Constants.
 */
public final class Constants {

  public static final String COSMOS_URL_KEYNAME = "CosmosUrl";
  public static final String COSMOS_KEY_KEYNAME = "CosmosKey";
  public static final String COSMOS_DATABASE_KEYNAME = "CosmosDatabase";
  public static final String COSMOS_COLLECTION_KEYNAME = "CosmosCollection";
  public static final String GENRE_DOCUMENT_TYPE = "Genre";
  public static final String MOVIE_DOCUMENT_TYPE = "Movie";
  public static final String ACTOR_DOCUMENT_TYPE = "Actor";

  public static final Integer DEFAULT_PAGE_SIZE = 100;
  public static final Integer MAX_PAGE_SIZE = 1000;
  public static final Integer MAX_PAGE_COUNT = 1000;
  public static final Integer MAX_DEGREE_PARALLELISM = 2;

  public static final String WEB_INSTANCE_ROLE = "WEBSITE_ROLE_INSTANCE_ID";
  public static final String WEB_INSTANCE_ROLE_ID = "WEBSITE_ROLE_INSTANCE_ID";

  public static final String AUTH_TYPE = "AUTH_TYPE";
  public static final String KEY_VAULT_NAME = "KEYVAULT_NAME";
  public static final String USE_MI = "MI";
  public static final String USE_MI_APPSVC = "MI_APPSVC";
  public static final String USE_CLI = "CLI";
  public static final String USE_VS = "VS";
  public static final String APP_INSIGHTS_KEY = "AppInsightsKey";

  public static final String ACTOR_CONTROLLER_EXCEPTION = "ActorControllerException";
  public static final String MOVIE_CONTROLLER_EXCEPTION = "MovieControllerException";
 
  public static final String KEYVAULT_NAME_ERROR_MSG = "KEYVAULT_NAME value is '{0}'"
      + " which does not meet the criteria must be 3-24 characters long, begin with a "
      + "character, may contain alphanumeric or hyphen, no repeating hyphens, and end with "
      + "alphanumeric.  Check ${KEYVAULT_NAME} in your environment variables.";

  private Constants() {
    // private constructor to hide pulbic constructor
  }
}
