package com.arondor.viewer.sample.connector.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arondor.viewer.client.api.document.DocumentFormatNotSupportedException;
import com.arondor.viewer.client.api.document.DocumentNotAvailableException;

/**
 * Filter to intercepts request to validate a token from a URL to a token validation service
 * Note: to activate this filter, uncomment the content of web-fragment.xml
 */
@Component(value = "arenderServletFilter")
public class ArenderServletFilter implements Filter
{

    private static final Logger LOGGER = Logger.getLogger(ArenderServletFilter.class);

    private static final String TOKEN_KEY = "token";

    private static final String AUTHENTICATED = "authenticated";

    private static final String SESSION_DURATION = "30";

    //    @Autowired
//    @Qualifier("myService")
    private String myService;

    @Override public void init(FilterConfig filterConfig) throws ServletException
    {
        // initialisation handled by Spring
    }

    /**
     * Method which intercepts all request matching with the url-pattern in arender web.xml
     * Blocks the process if the transition is not validated
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        if (isAuthenticated(session))
        {
            chain.doFilter(request, response);
            return;
        }
        LOGGER.debug("Intercept request to validate the token");
        if (request.getParameter(TOKEN_KEY) != null)
        {
            try
            {
                if (canProcessRequest(httpRequest))
                {
                    chain.doFilter(request, response);
                    return;
                }
            }
            catch (DocumentNotAvailableException | DocumentFormatNotSupportedException e)
            {
                LOGGER.error("Request stopped", e);
            }
        }
        else
        {
            LOGGER.error("Request stopped: URL should contain parameter " + TOKEN_KEY);
        }
        ((HttpServletResponse) response).sendRedirect("/ARender/error.jsp");
    }

    /**
     * Check if the token is a valid token returns true if it is, false otherwise
     */
    private boolean canProcessRequest(HttpServletRequest httpRequest)
            throws IOException, ServletException, DocumentNotAvailableException, DocumentFormatNotSupportedException
    {
//        boolean isTokenValidated = myService.validateToken(httpRequest);
        boolean isTokenValidated = true;
        if(isTokenValidated)
        {
            LOGGER.debug("Adding " + AUTHENTICATED + " value to the session");
            httpRequest.getSession().setAttribute(AUTHENTICATED, true);
            httpRequest.getSession().setMaxInactiveInterval(Integer.parseInt(SESSION_DURATION));
        }
        return isTokenValidated;
    }

    /**
     * Check if the user is already authenticated to myService
     * ie. the user session has to have the attribute "authenticated" set to true in its attribute
     */
    private boolean isAuthenticated(HttpSession session)
    {
        boolean isAuthenticated =
                session != null && session.getAttribute(AUTHENTICATED) != null && session
                        .getAttribute(AUTHENTICATED) instanceof Boolean && (Boolean) session
                        .getAttribute(AUTHENTICATED);
        if (isAuthenticated)
        {
            LOGGER.debug("User already authenticated");
            return true;
        }
        else
        {
            LOGGER.debug("User not authenticated");
        }
        return false;
    }

    @Override public void destroy()
    {
        // Destruction handled by Spring
    }
}
