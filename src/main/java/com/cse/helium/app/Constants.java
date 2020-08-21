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

  public static final String INVALID_QUERY_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid q (search) parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"q\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidSearchParameter\",\n"
      + "         \"minLength\":2,\n"
      + "         \"maxLength\":20,\n"
      + "         \"characterTypes\":[\n"
      + "            \"lowerCase\",\n"
      + "            \"upperCase\",\n"
      + "            \"number\",\n"
      + "            \"symbol\"\n"
      + "         ]\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_PAGESIZE_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid PageSize parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"pageSize\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidPageSizeParameter\",\n"
      + "         \"minValue\":1,\n"
      + "         \"maxValue\":1000,\n"
      + "         \"valueTypes\":[\n"
      + "            \"integer\"\n"
      + "         ]\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_PAGENUM_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid PageNumber parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"pageNumber\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidPageNumberParameter\",\n"
      + "         \"minValue\":1,\n"
      + "         \"maxValue\":10000,\n"
      + "         \"valueTypes\":[\n"
      + "            \"integer\"\n"
      + "         ]\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_GENRE_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid Genre parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"genre\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidGenreParameter\",\n"
      + "         \"minLength\":3,\n"
      + "         \"maxLength\":20,\n"
      + "         \"valueTypes\":[\n"
      + "            \"string\"\n"
      + "         ]\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_YEAR_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid Year parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"year\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidYearParameter\",\n"
      + "         \"minValue\":1874,\n"
      + "         \"maxValue\":2025,\n"
      + "         \"valueTypes\":[\n"
      + "            \"integer\"\n"
      + "         ]\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_RATING_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid Rating parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"rating\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidRatingParameter\",\n"
      + "         \"minValue\":0,\n"
      + "         \"maxValue\":10,\n"
      + "         \"valueTypes\":[\n"
      + "            \"double\"\n"
      + "         ]\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_ACTORID_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid Actor ID parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"actorId\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidActorIDParameter\"\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_MOVIEID_MESSAGE = "{\n"
      + "   \"error\":{\n"
      + "      \"code\":\"BadArgument\",\n"
      + "      \"message\":\"Invalid Movie ID parameter\",\n"
      + "      \"statusCode\":400,\n"
      + "      \"target\":\"movieId\",\n"
      + "      \"innererror\":{\n"
      + "         \"code\":\"InvalidMovieIDParameter\"\n"
      + "      }\n"
      + "   }\n"
      + "}";

  private Constants() {
    // private constructor to hide pulbic constructor
  }
}
