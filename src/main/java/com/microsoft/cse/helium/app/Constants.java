package com.microsoft.cse.helium.app;

/**
 * Constants.
 */
public final class Constants {

  public static final String COSMOS_URL_KEYNAME = "CosmosUrl";
  public static final String COSMOS_KEY_KEYNAME = "CosmosKey";
  public static final String COSMOS_DATABASE_KEYNAME = "CosmosDatabase";
  public static final String COSMOS_COLLECTION_KEYNAME = "CosmosCollection";

  public static final Integer DEFAULT_PAGE_SIZE = 100;
  public static final Integer MAX_PAGE_SIZE = 1000;
  public static final Integer MAX_PAGE_COUNT = 1000;
  public static final String WEB_INSTANCE_ROLE = "WEBSITE_ROLE_INSTANCE_ID";
  public static final Integer MAX_DEGREE_PARALLELISM = 2;

  public static final String AUTH_TYPE = "AUTH_TYPE";
  public static final String KEY_VAULT_NAME = "KEYVAULT_NAME";
  public static final String USE_MSI = "MSI";
  public static final String USE_MSI_APPSVC = "MSI_APPSVC";
  public static final String USE_CLI = "CLI";
  public static final String USE_VS = "VS";
  public static final String ACTOR_CONTROLLER_EXCEPTION = "ActorControllerException";
  public static final String MOVIE_CONTROLLER_EXCEPTION = "MovieControllerException";

}
