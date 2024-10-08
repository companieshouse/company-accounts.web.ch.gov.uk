package uk.gov.companieshouse.web.accounts.interceptor;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.session.SessionService;

@Component
public class UserDetailsInterceptor implements HandlerInterceptor {

    private static final String USER_EMAIL = "userEmail";

    private static final String SIGN_IN_KEY = "signin_info";
    private static final String USER_PROFILE_KEY = "user_profile";
    private static final String EMAIL_KEY = "email";

    @Autowired
    SessionService sessionService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        if (modelAndView == null) {
            return;
        }

        if ((isGetRequest(request) && !isResumeRequest(request))
                || isPostRequestRedirect(request, modelAndView)) {

            addUserDetailsToModelAndView(modelAndView);
        }
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.GET.toString());
    }

    private boolean isPostRequestRedirect(HttpServletRequest request, ModelAndView modelAndView) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.POST.toString()) &&
                !modelAndView.getViewName().startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
    }

    private boolean isResumeRequest(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/resume");
    }

    private void addUserDetailsToModelAndView(ModelAndView modelAndView) {
        Map<String, Object> sessionData = sessionService.getSessionDataFromContext();
        Map<String, Object> signInInfo = (Map<String, Object>) sessionData.get(SIGN_IN_KEY);
        if (signInInfo != null) {
            Map<String, Object> userProfile = (Map<String, Object>) signInInfo
                    .get(USER_PROFILE_KEY);

            if (userProfile != null) {
                modelAndView.addObject(USER_EMAIL, userProfile.get(EMAIL_KEY));
            }
        }
    }
}
