package com.cse.helium.app.middleware;

import java.net.InetSocketAddress;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestLogger implements WebFilter {

  private static final Logger logger =   LogManager.getLogger();
 
  /**
   * filter gathers the request and response metadata and logs
   *   the results to console.
  */
  @Override
  public Mono<Void> filter(ServerWebExchange serverWebExchange, 
      WebFilterChain webFilterChain) {
    // get request metadata
    String requestAddress = getRequestAddress(serverWebExchange.getRequest().getRemoteAddress());
    String pathQueryString = getPathQueryString(serverWebExchange.getRequest());

    // set start time
    long startTime = System.currentTimeMillis();
      
    // process next handler
    return webFilterChain.filter(serverWebExchange).doFinally(signalType -> {
      // compute request duration and get status code
      long duration = System.currentTimeMillis() - startTime;
      int statusCode = serverWebExchange.getResponse().getStatusCode().value();
      
      // don't log favicon.ico 404s
      if (pathQueryString.startsWith("/favicon.ico")) {
        return;
      }

      // don't log if log level >= warn but response code < 400
      if (logger.getLevel().isMoreSpecificThan(Level.WARN) && statusCode < 400) {
        return;
      }

      // log results to console
      logger.info(String.format(
          "%s\t%s\t%s\t%s", 
          statusCode, duration, requestAddress, pathQueryString));
    });
  }

  /** getRequestAddress returns the request IP address if it exists. */
  private String getRequestAddress(InetSocketAddress requestAddress) {
    if (requestAddress != null) {
      return requestAddress.getHostString();
    }
    return "";
  }

  /** getPathQueryString returns the path and query string if it exists. */
  private String getPathQueryString(ServerHttpRequest request) {
    String pathQueryString = request.getURI().getPath();
    String query = request.getURI().getQuery();
    if (query != null) {
      pathQueryString += "?" + query;
    }

    return pathQueryString;
  }
}
