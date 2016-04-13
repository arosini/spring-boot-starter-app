package ar.filter;

import ar.test.util.TestUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.support.CronTrigger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tests for the {@link RequestLimitFilter} class.
 * 
 * @author adam
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestLimitFilterTests {

  @InjectMocks
  private RequestLimitFilter requestLimitFilter;

  @Spy
  private AtomicInteger requestCount;

  @Mock
  private FilterChain filterChain;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private PrintWriter printWriter;

  @Before
  public void before() throws IOException {
    requestLimitFilter.setRequestLimit(100);
    Mockito.when(response.getWriter()).thenReturn(printWriter);
  }

  @Test
  public void doFilterInternal_atTheLimit() throws ServletException, IOException {
    requestCount.set(99);

    requestLimitFilter.doFilterInternal(request, response, filterChain);
    requestLimitFilter.doFilterInternal(request, response, filterChain);

    Mockito.verify(filterChain).doFilter(request, response);
  }

  @Test
  public void doFilterInternal_overTheLimit() throws ServletException, IOException {
    requestCount.set(100);

    requestLimitFilter.doFilterInternal(request, response, filterChain);
    requestLimitFilter.doFilterInternal(request, response, filterChain);

    Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
  }

  @Test
  public void doFilterInternal_underTheLimit() throws ServletException, IOException {
    requestCount.set(98);

    requestLimitFilter.doFilterInternal(request, response, filterChain);
    requestLimitFilter.doFilterInternal(request, response, filterChain);

    Mockito.verify(filterChain, Mockito.times(2)).doFilter(request, response);
  }

  @Test
  public void resetRequestLimit() {
    requestCount.set(1);
    Assert.assertEquals(requestCount.get(), 1);

    requestLimitFilter.resetRequestCount();
    Assert.assertEquals(requestCount.get(), 0);
  }

  @Test
  public void resetRequestLimit_cronExpression() {
    CronTrigger cronTrigger = new CronTrigger("0 0 4 * * SUN");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2016);
    calendar.set(Calendar.MONTH, Calendar.MARCH);
    calendar.set(Calendar.DATE, 6);
    calendar.set(Calendar.HOUR_OF_DAY, 4);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    Date nextExecutionTime = cronTrigger.nextExecutionTime(TestUtils.createTriggerContext(calendar.getTime()));
    calendar.set(Calendar.DATE, 13);
    Assert.assertEquals(calendar.getTime(), nextExecutionTime);

    nextExecutionTime = cronTrigger.nextExecutionTime(TestUtils.createTriggerContext(calendar.getTime()));
    calendar.set(Calendar.DATE, 20);
    Assert.assertEquals(calendar.getTime(), nextExecutionTime);
  }

}
