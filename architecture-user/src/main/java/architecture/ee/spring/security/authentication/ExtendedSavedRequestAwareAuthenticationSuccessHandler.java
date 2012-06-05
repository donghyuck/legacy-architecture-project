package architecture.ee.spring.security.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import architecture.ee.util.OutputFormat;

public class ExtendedSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	protected final Log logger = LogFactory.getLog(this.getClass());

    private RequestCache requestCache = new HttpSessionRequestCache();

    private boolean useReferer = false;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        if (isAlwaysUseDefaultTargetUrl() || StringUtils.hasText(request.getParameter(getTargetUrlParameter()))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);
        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        
    }
    

    /**
     * Builds the target URL according to the logic defined in the main class Javadoc.
     */
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
    	
    	logger.debug("isAlwaysUseDefaultTargetUrl:" + isAlwaysUseDefaultTargetUrl() );
    	logger.debug("defaultTargetUrl:" + getDefaultTargetUrl() );
    	logger.debug("targetUrlParameter:" + getTargetUrlParameter() );
    	
        if (isAlwaysUseDefaultTargetUrl()) {        	
            return getDefaultTargetUrl();            
        }

        // Check for the parameter and use that if available
        String targetUrl = request.getParameter(getTargetUrlParameter());
        
        logger.debug("targetUrl:" + targetUrl );
        
        if (StringUtils.hasText(targetUrl)) {
            try {
            	targetUrl = determineTargetUrl(request, response, targetUrl);
                targetUrl = URLDecoder.decode(targetUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("UTF-8 not supported. Shouldn't be possible");
            }
            logger.debug("Found targetUrlParameter in request: " + targetUrl);
            return targetUrl;
        }

        if (useReferer && !StringUtils.hasLength(targetUrl)) {
            targetUrl = request.getHeader("Referer");
            logger.debug("Using Referer header: " + targetUrl);
        }

        if (!StringUtils.hasText(targetUrl)) {
            targetUrl = getDefaultTargetUrl();
            logger.debug("Using default Url: " + targetUrl);
        }
        
        

        targetUrl = determineTargetUrl(request, response, targetUrl);
        
        return targetUrl;
    }
    
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, String targetUrl){
        
    	String dataTypeString = request.getParameter("dataType");
        OutputFormat dataType = OutputFormat.stingToOutputFormat(dataTypeString);
        String targetUrlToUse = targetUrl;
        
        if( targetUrl.indexOf("?") > 0 )
        	targetUrlToUse = targetUrl + "&dataType=" + dataType ;
        else
        	targetUrlToUse = targetUrl + "?dataType=" + dataType ;
        	
    	return targetUrlToUse;
    }

    
    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
    
    public void setUseReferer(boolean useReferer) {
        this.useReferer = useReferer;
        super.setUseReferer(useReferer);
    }
}
