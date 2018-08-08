package uk.gov.companieshouse.web.accounts.interceptor;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import uk.gov.companieshouse.session.handler.SessionHandler;

@Component
public class UserDetailsInterceptor extends HandlerInterceptorAdapter {

    private static final String USER_EMAIL = "userEmail";

    private static final String SIGN_IN_KEY = "signin_info";
    private static final String USER_PROFILE_KEY = "user_profile";
    private static final String EMAIL_KEY = "email";

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            Map<String, Object> sessionData = SessionHandler.getSessionDataFromContext();
            Map<String, Object> signInInfo = (Map<String, Object>) sessionData.get(SIGN_IN_KEY);
            if (signInInfo != null) {
                Map<String, Object> userProfile = (Map<String, Object>) signInInfo.get(USER_PROFILE_KEY);
                modelAndView.getModelMap().addAttribute(USER_EMAIL, userProfile.get(EMAIL_KEY));
            }
        }
    }
}
