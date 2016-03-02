package ar.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Limits the number of requests that can be made each week to a configured value.
 * 
 * @author adam
 *
 */
@Component
@ConfigurationProperties(prefix = "request-limit-filter")
public class RequestLimitFilter extends OncePerRequestFilter {

  private int requestLimit;

  private AtomicInteger requestCount = new AtomicInteger();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (requestCount.incrementAndGet() <= requestLimit) {
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter()
          .write(
              "{ \"errors\": [ { \"message\": \"The request limit for this week has been reached (sorry, I don't want to be charged).\" } ] }");
    }
  }

  /**
   * Resets the counter at 4:00 AM every Sunday.
   */
  @Scheduled(cron = "0 0 4 * * SUN")
  public void resetRequestCount() {
    requestCount.set(0);
  }

  /**
   * Returns the maximum number of requests that can be made before the counter is reset.
   * 
   * @return The maximum number of requests that can be made before the counter is reset.
   */
  public int getRequestLimit() {
    return requestLimit;
  }

  /**
   * Sets the maximum number of requests that can be made before the counter is reset.
   * 
   * @param requestLimit The maximum number of requests that can be made before the counter is reset.
   */
  public void setRequestLimit(int requestLimit) {
    this.requestLimit = requestLimit;
  }

}
