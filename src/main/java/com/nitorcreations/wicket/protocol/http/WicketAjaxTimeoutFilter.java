package com.nitorcreations.wicket.protocol.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A filter that tells Wicket's ajax functionality to refresh the page if the Ajax header is set and there is no active
 * session for the current user. This causes a normal http request being intercepted by normal security patterns, e.g.,
 * forward to login page.
 * <p/>
 * <strong>NOTE:</strong> Only works with Wicket 6. Previous versions need different ajax response.
 *
 * @author Reko Jokelainen / Nitor Creations
 */
public class WicketAjaxTimeoutFilter implements Filter {
    public static final String AJAX_HEADER_PARAM = "ajaxHeaderName";
    public static final String AJAX_HEADER_DEFAULT = "Wicket-Ajax";
    private String ajaxHeaderName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        final String configured = filterConfig.getInitParameter(AJAX_HEADER_PARAM);
        ajaxHeaderName = configured == null ? AJAX_HEADER_DEFAULT : configured;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;
        if (!hasSession(req) && isWicketAjax(req)) {
            @SuppressWarnings("resource")
            final PrintWriter writer = resp.getWriter();
            writer.append(getResponseString());
            writer.flush();
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isWicketAjax(HttpServletRequest req) {
        return req.getHeader(ajaxHeaderName) != null;
    }

    private boolean hasSession(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    private String getResponseString() {
        return "<ajax-response><evaluate><![CDATA[(function(){window.location.reload();})();]]></evaluate></ajax-response>";
    }

    @Override
    public void destroy() {
        // do nothing
    }

    public String getAjaxHeaderName() {
        return ajaxHeaderName;
    }

    public void setAjaxHeaderName(String ajaxHeaderName) {
        this.ajaxHeaderName = ajaxHeaderName;
    }
}
