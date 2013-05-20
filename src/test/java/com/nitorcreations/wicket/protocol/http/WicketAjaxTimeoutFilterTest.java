package com.nitorcreations.wicket.protocol.http;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WicketAjaxTimeoutFilterTest {

    private static final String expectedResponse = "<ajax-response><evaluate><![CDATA[(function(){window.location.reload();})();]]></evaluate></ajax-response>";

    private static final String testHeader = "Test-Header";

    @InjectMocks
    WicketAjaxTimeoutFilter filter;

    @Mock
    FilterConfig filterConfig;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain chain;

    @Mock
    HttpSession session;

    @Mock
    PrintWriter writer;

    @Before
    public void create() throws ServletException, IOException {
        MockitoAnnotations.initMocks(this);

        when(response.getWriter()).thenReturn(writer);
        when(filterConfig.getInitParameter(WicketAjaxTimeoutFilter.AJAX_HEADER_PARAM)).thenReturn(testHeader);

        filter.init(filterConfig);
        assertThat(filter.getAjaxHeaderName(), equalTo(testHeader));
    }

    @Test
    public void defaultHeaderName() throws ServletException {
        filter.init(Mockito.mock(FilterConfig.class));
        assertThat(filter.getAjaxHeaderName(), equalTo(WicketAjaxTimeoutFilter.AJAX_HEADER_DEFAULT));
    }

    @Test
    public void noSessionAndAjaxHeaderSet() throws IOException, ServletException {
        when(request.getHeader(testHeader)).thenReturn("value");
        when(request.getSession(false)).thenReturn(null);
        filter.doFilter(request, response, chain);

        verifyZeroInteractions(chain);
        verify(writer).append(expectedResponse);
        verify(writer).flush();
    }

    @Test
    public void noSessionAndNoHeader() throws IOException, ServletException {
        when(request.getHeader(testHeader)).thenReturn(null);
        when(request.getSession(false)).thenReturn(null);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void ajaxHeaderSetWhenSessionExists() throws IOException, ServletException {
        when(request.getHeader(testHeader)).thenReturn("value");
        when(request.getSession(false)).thenReturn(session);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }
}
