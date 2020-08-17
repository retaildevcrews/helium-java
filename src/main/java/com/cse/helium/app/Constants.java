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
      + "         \"minLength\":\"2\",\n"
      + "         \"maxLength\":\"20\",\n"
      + "         \"characterTypes\":[\n"
      + "            \"lowerCase\",\n"
      + "            \"upperCase\",\n"
      + "            \"number\",\n"
      + "            \"symbol\"\n"
      + "         ]\n"
      + "      }\n"
      + "   }\n"
      + "}";
  public static final String INVALID_PAGESIZE_MESSAGE = "{"
      + "   \"error\":{"
      + "      \"code\":\"BadArgument\","
      + "      \"message\":\"Invalid PageSize parameter\","
      + "      \"statusCode\":400,"
      + "      \"target\":\"pageSize\","
      + "      \"innererror\":{"
      + "         \"code\":\"InvalidPageSizeParameter\","
      + "         \"minValue\":\"1\","
      + "         \"maxValue\":\"1000\","
      + "         \"valueTypes\":["
      + "            \"integer\""
      + "         ]"
      + "      }"
      + "   }"
      + "}";
  public static final String INVALID_PAGENUM_MESSAGE = "{"
      + "   \"error\":{"
      + "      \"code\":\"BadArgument\","
      + "      \"message\":\"Invalid PageNumber parameter\","
      + "      \"statusCode\":400,"
      + "      \"target\":\"pageNumber\","
      + "      \"innererror\":{"
      + "         \"code\":\"InvalidPageNumberParameter\","
      + "         \"minValue\":\"1\","
      + "         \"maxValue\":\"10000\","
      + "         \"valueTypes\":["
      + "            \"integer\""
      + "         ]"
      + "      }"
      + "   }"
      + "}";
  public static final String INVALID_GENRE_MESSAGE = "{"
      + "   \"error\":{"
      + "      \"code\":\"BadArgument\","
      + "      \"message\":\"Invalid Genre parameter\","
      + "      \"statusCode\":400,"
      + "      \"target\":\"genre\","
      + "      \"innererror\":{"
      + "         \"code\":\"InvalidGenreParameter\","
      + "         \"minLength\":\"3\","
      + "         \"maxLength\":\"20\","
      + "         \"valueTypes\":["
      + "            \"string\""
      + "         ]"
      + "      }"
      + "   }"
      + "}";
  public static final String INVALID_YEAR_MESSAGE = "{"
      + "   \"error\":{"
      + "      \"code\":\"BadArgument\","
      + "      \"message\":\"Invalid Year parameter\","
      + "      \"statusCode\":400,"
      + "      \"target\":\"year\","
      + "      \"innererror\":{"
      + "         \"code\":\"InvalidYearParameter\","
      + "         \"minLength\":\"1874\","
      + "         \"maxLength\":\"2025\","
      + "         \"valueTypes\":["
      + "            \"integer\""
      + "         ]"
      + "      }"
      + "   }"
      + "}";
  public static final String INVALID_RATING_MESSAGE = "{"
      + "   \"error\":{"
      + "      \"code\":\"BadArgument\","
      + "      \"message\":\"Invalid Rating parameter\","
      + "      \"statusCode\":400,"
      + "      \"target\":\"rating\","
      + "      \"innererror\":{"
      + "         \"code\":\"InvalidRatingParameter\","
      + "         \"minLength\":\"0\","
      + "         \"maxLength\":\"10\","
      + "         \"valueTypes\":["
      + "            \"double\""
      + "         ]"
      + "      }"
      + "   }"
      + "}";
  public static final String INVALID_ACTORID_MESSAGE = "{"
      + "   \"error\":{"
      + "      \"code\":\"BadArgument\","
      + "      \"message\":\"Invalid Actor ID parameter\","
      + "      \"statusCode\":400,"
      + "      \"target\":\"actorId\","
      + "      \"innererror\":{"
      + "         \"code\":\"InvalidActorIDParameter\""
      + "      }"
      + "   }"
      + "}";
  public static final String INVALID_MOVIEID_MESSAGE = "{"
      + "   \"error\":{"
      + "      \"code\":\"BadArgument\","
      + "      \"message\":\"Invalid Movie ID parameter\","
      + "      \"statusCode\":400,"
      + "      \"target\":\"movieId\","
      + "      \"innererror\":{"
      + "         \"code\":\"InvalidMovieIDParameter\""
      + "      }"
      + "   }"
      + "}";

  private Constants() {
    // private constructor to hide pulbic constructor
  }
}
